package com.upgrad.FoodOrderingApp.service.businness;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* java imports */
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.UUID;
import javax.transaction.Transactional;

/* project imports */
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

@Service
public class CustomerService {
  @Autowired
  CustomerDao customerDao;

  @Autowired
  PasswordCryptographyProvider cryptoProvider;

  /* performs the necessary processes to handle customer sign up */
  @Transactional
  public String signup(String firstName, String lastName, String email, String contact, String password) {
    System.out.println(">_ creating new customer and sending to dao...");
    CustomerEntity newCustomer = this.createNewCustomerEntity(firstName, lastName, email, contact, password);
    customerDao.registerNewCustomer(newCustomer);
    return newCustomer.getUuid();
  }

  /* performs the necessary validation for the new sign up request that we are receiving */
  public boolean validateSignUpRequest(String firstName, String lastName, String email, String contact, String password) 
  throws SignUpRestrictedException {
    System.out.println(">_ validating new sign up request....");

    /* only last name is optional */
    if (
      !this.isStringNullOrEmpty(firstName) ||
      !this.isStringNullOrEmpty(email) || 
      !this.isStringNullOrEmpty(contact) || 
      !this.isStringNullOrEmpty(password)
    ) {
      throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
    }
    else if (!this.isValidEmailString(email)) {
      throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
    }
    else if (!this.isValidContactNumber(contact)) {
      throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
    }
    else if(!this.isValidAndStrongPassword(password)) {
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

  /* is null or empty helper function */
  public boolean isStringNullOrEmpty(String string) {
    if (string != null && !string.isEmpty()) {
      return true;
    }
    else {
      return false;
    }
  }

  /* is valid email format helper function */
  public boolean isValidEmailString(String email) {
    String regex = "^[A-Z0-9a-z._%+-]+@[A-Z0-9a-z.-]+\\.[A-Za-z]{2,6}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  /* is valid contact number helper function */
  public boolean isValidContactNumber(String contact) {
    return (contact.length() <= 10) && (contact.matches("[0-9]+"));
  }

  /* is valid and strong password helper function */
  public boolean isValidAndStrongPassword(String password) {
    Pattern special = Pattern.compile("[\\[\\]#@$%&*!^]");
    Matcher hasSpecial = special.matcher(password);
    return (password.length() >= 8) && hasSpecial.find();
  }

  /* helps create a new customer entity */
  public CustomerEntity createNewCustomerEntity(String firstName, String lastName, String email, String contact, String password) {
    CustomerEntity newCustomer = new CustomerEntity();
    newCustomer.setFirstName(firstName);
    newCustomer.setLastName(lastName);
    newCustomer.setContactNumber(contact);
    newCustomer.setEmail(email);
    newCustomer.setUuid(UUID.randomUUID().toString());

    /* encryption of password */
    String[] arrayOfEncryptedString = this.cryptoProvider.encrypt(password);
    newCustomer.setSalt(arrayOfEncryptedString[0]);
    newCustomer.setPassword(arrayOfEncryptedString[1]);

    return newCustomer;
  }
}
