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
      !serviceUtility.isStringNullOrEmpty(firstName) ||
      !serviceUtility.isStringNullOrEmpty(email) || 
      !serviceUtility.isStringNullOrEmpty(contact) || 
      !serviceUtility.isStringNullOrEmpty(password)
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
}
