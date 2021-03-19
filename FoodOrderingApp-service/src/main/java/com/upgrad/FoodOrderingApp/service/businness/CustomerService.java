package com.upgrad.FoodOrderingApp.service.businness;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* java imports */
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.UUID;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

/* project imports */
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;

@Service
public class CustomerService {
  @Autowired
  private CustomerDao customerDao;

  @Autowired
  private ServiceUtility serviceUtility;

  @Autowired
  private PasswordCryptographyProvider cryptoProvider;

  /* performs the necessary processes to handle customer sign up */
  @Transactional
  public String signup(String firstName, String lastName, String email, String contact, String password) {
    System.out.println(">_ creating new customer and sending to dao...");
    CustomerEntity newCustomer = serviceUtility.createNewCustomerEntity(firstName, lastName, email, contact, password);
    customerDao.registerNewCustomer(newCustomer);
    return newCustomer.getUuid();
  }

  /* performs the necessary processes to handler customer sign in */
  @Transactional
  public HashMap signIn(String username, String password) throws AuthenticationFailedException {
    System.out.println(">_ performing sign in process...");
    CustomerEntity registeredCustomer = customerDao.getUserByContactNumber(username);
    /* if not found */
    if (registeredCustomer == null) {
      throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
    }
    /* here it means we're ok */
    else {
      String hashedPassword = cryptoProvider.encrypt(password, registeredCustomer.getSalt());
      if (!hashedPassword.equals(registeredCustomer.getPassword())) {
        throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
      }
      else {
        /* build the jwt */
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(hashedPassword);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        String jwt = jwtTokenProvider.generateToken(registeredCustomer.getUuid(), now, expiresAt);

        /* make a registry in the customer_auth table in the database */
        customerDao.registerLoginSession(registeredCustomer, jwt, now, expiresAt);

        /* fetch all the other pieces required */
        String uuid = registeredCustomer.getUuid();
        String firstName = registeredCustomer.getFirstName();
        String lastName = registeredCustomer.getLastName();
        String email = registeredCustomer.getEmail();
        String contact = registeredCustomer.getContactNumber();
        String message = "LOGGED IN SUCCESSFULLY";
        HashMap loginHashMap = serviceUtility.buildLoginResponse(uuid, firstName, lastName, email, contact, message, jwt, now, expiresAt);

        /* return */
        return loginHashMap;
      }
    }
  }

  /* performs the necessary validation for the new sign up request that we are receiving */
  public boolean validateSignUpRequest(String firstName, String lastName, String email, String contact, String password) 
  throws SignUpRestrictedException {
    System.out.println(">_ validating new sign up request....");

    /* only last name is optional */
    if (
      serviceUtility.isStringNullOrEmpty(firstName) ||
      serviceUtility.isStringNullOrEmpty(email) || 
      serviceUtility.isStringNullOrEmpty(contact) || 
      serviceUtility.isStringNullOrEmpty(password)
    ) {
      throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
    }
    else if (!serviceUtility.isValidEmailString(email)) {
      throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
    }
    else if (!serviceUtility.isValidContactNumber(contact)) {
      throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
    }
    else if(!serviceUtility.isValidAndStrongPassword(password)) {
      throw new SignUpRestrictedException("SGR-004", "Weak password!");
    }
    else {
      CustomerEntity entityWasFound = customerDao.getUserByContactNumber(contact);
      if (entityWasFound != null) {
        throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
      }
      else {
        return true;
      }
    }
  }

  /* performs the necessary processes to validate a particular jwt */
  public CustomerAuthEntity validateAccessToken(String jwt) 
  throws AuthorizationFailedException {
    /*1. lets check if there is a registry in the customer_auth table for this jwt */
    CustomerAuthEntity customerAuthEntity = customerDao.getCustomerEntityByAccessToken(jwt);
    System.out.println(customerAuthEntity);
    /* if null */
    if (customerAuthEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
    }
    else {
      ZonedDateTime logoutStamp = customerAuthEntity.getLogoutAt();
      /* if already logged out */
      if (logoutStamp != null) {
        throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
      }
      else {
        boolean isJwtValid = serviceUtility.checkIfTokenHasExpired(customerAuthEntity.getExpiresAt().toString());
        if (!isJwtValid) {
          customerAuthEntity.setLogoutAt(ZonedDateTime.now());
          customerDao.updateCustomerAuthEntity(customerAuthEntity);
          throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        else {
          return customerAuthEntity;
        }
      }
    }
  }

  /* performs the signout process with the dao */
  @Transactional
  public void performSignOutProcess(CustomerAuthEntity authEntity) {
    customerDao.updateCustomerAuthEntity(authEntity);
  }

  /* performs validation on the update request for a customer and then if its valid, then will make updates in the db */
  @Transactional
  public CustomerEntity updateCustomerInfoIfValid(Integer customerId, String firstName, String lastName) 
  throws UpdateCustomerException, Exception {
    /* check if the first name is given otherwise throw error */
    if (serviceUtility.isStringNullOrEmpty(firstName)) {
      throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
    }
    else {
      /* here it means we're ok, now fetch the current customer entity from the db from customer table */
      CustomerEntity customer = customerDao.getCustomerEntityById(customerId);
      
      /* update the details for the current customer entity and then push the request to the dao for the update */
      customer.setFirstName(firstName);
      customer.setLastName(lastName);
      customerDao.updateCustomerEntity(customer);

      /* finally send the updated customer details to the controller */
      return customer;
    }
  }

  /* peforms validation on the update request for a customer password (old vs new), then will make updates in the db */
  @Transactional
  public CustomerEntity updateCustomerPasswordIfValid(Integer customerId, String oldPassword, String newPassword)
  throws UpdateCustomerException, Exception {
    /* first fetch the respective customer entity from the db based on the customer id */
    CustomerEntity customer = customerDao.getCustomerEntityById(customerId);

    /* check to see if the old password field is a falsy value */
    if (
      serviceUtility.isStringNullOrEmpty(oldPassword) ||
      serviceUtility.isStringNullOrEmpty(newPassword)) {
      throw new UpdateCustomerException("UCR-003", "No field should be empty");
    }
    else {
      /* check to see if the old password is the correct password for the customer entity */
      String hashedPassword = cryptoProvider.encrypt(oldPassword, customer.getSalt());
      if (!hashedPassword.equals(customer.getPassword())) {
        throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
      }
      /* validate the strength of the new password */
      else if (!serviceUtility.isValidAndStrongPassword(newPassword)) {
        throw new UpdateCustomerException("UCR-001", "Weak password!");
      }
      else {
        /* update the customer entity with the new password details */
        String[] arrayOfEncryptedString = this.cryptoProvider.encrypt(newPassword);
        customer.setSalt(arrayOfEncryptedString[0]);
        customer.setPassword(arrayOfEncryptedString[1]);
        customerDao.updateCustomerEntity(customer);

        /* finally return the value to the controller with the updated details */
        return customer;
      }
    }
  }
}
