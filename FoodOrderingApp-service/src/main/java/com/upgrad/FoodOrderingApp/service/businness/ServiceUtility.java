package com.upgrad.FoodOrderingApp.service.businness;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/* project imports */
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

/* java imports */
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.UUID;
import java.util.HashMap;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

@Component
public class ServiceUtility {
  @Autowired
  private PasswordCryptographyProvider cryptoProvider;

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
  
  /* helps create a new customer entity */
  public CustomerEntity createNewCustomerEntity(String firstName, String lastName, String email, String contact, String password) {
    String[] arrayOfEncryptedString = this.cryptoProvider.encrypt(password);

    CustomerEntity newCustomer = new CustomerEntity();
    newCustomer.setFirstName(firstName);
    newCustomer.setLastName(lastName);
    newCustomer.setContactNumber(contact);
    newCustomer.setEmail(email);
    newCustomer.setUuid(UUID.randomUUID().toString());
    newCustomer.setSalt(arrayOfEncryptedString[0]);
    newCustomer.setPassword(arrayOfEncryptedString[1]);

    return newCustomer;
  }

  /* helps build the login response hash map */
  public HashMap<String, Object> buildLoginResponse(
    String id, String firstName, String lastName, String email, String contact, String message,
    String jwt, ZonedDateTime now, ZonedDateTime expiresAt
  ) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", id);
    map.put("firstname", firstName);
    map.put("lastname", lastName);
    map.put("email", email);
    map.put("contact", contact);
    map.put("message", message);
    map.put("jwt", jwt);
    map.put("now", now);
    map.put("expiry", expiresAt);
    return map;
  }

  /* is valid and strong password helper function */
  public boolean isValidAndStrongPassword(String password) {
    Pattern special = Pattern.compile("[\\[\\]#@$%&*!^]");
    Matcher hasSpecial = special.matcher(password);
    return (password.length() >= 8) && hasSpecial.find();
  }
}
