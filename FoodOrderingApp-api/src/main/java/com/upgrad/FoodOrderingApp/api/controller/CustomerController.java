package com.upgrad.FoodOrderingApp.api.controller;

/* spring imports */
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

/* java imports */
import java.util.Base64;
import java.util.HashMap;

/* project imports */
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;

@RestController
public class CustomerController {
  @Autowired
  CustomerService customerService;

  /* @CrossOrigin(origins = "http://localhost:8080") */
  @RequestMapping (
    path = "/customer/signup", 
    method = RequestMethod.POST, 
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity<SignupCustomerResponse> signup(@RequestBody SignupCustomerRequest signUpRequest) 
  throws SignUpRestrictedException, Exception {
    /* we need these... */
    String firstName = signUpRequest.getFirstName();
    String lastName = signUpRequest.getLastName();
    String email = signUpRequest.getEmailAddress();
    String contact = signUpRequest.getContactNumber();
    String password = signUpRequest.getPassword();

    /* 1. we will first perform basic validation process with a service */
    boolean isValid = customerService.validateSignUpRequest(firstName, lastName, email, contact, password);
    /* 2. if you're here, it means that the validation is ok and now we can create an instance on the db */
    String newUuid = customerService.signup(firstName, lastName, email, contact, password);
    /* 3. finally send the details to the client side as a response */
    SignupCustomerResponse signUpResponse = new SignupCustomerResponse();
    signUpResponse.setId(newUuid);
    signUpResponse.setStatus("CUSTOMER SUCCESSFULLY REGISTERED");
    return new ResponseEntity(signUpResponse, HttpStatus.OK);
  }

  @RequestMapping (
    path = "/customer/login", 
    method = RequestMethod.POST, 
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") String authorization) 
  throws AuthenticationFailedException, Exception {
    /* 1. lets check if the authorization header is in a proper format and then proceed, otherwise throw error */
    if (authorization.indexOf("Basic ") == -1) {
      throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
    }
    else {
      /* 2. authorization header format is ok, proceed with the next step - convert the authorization string to username and password strings */
      String authUserName;
      String authPassword;
      byte[] decode;

      try {
        decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
      }
      catch (Exception e) {
        throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
      }
      
      String decodedText = new String(decode);
      String[] decodedArray = decodedText.split(":");
      authUserName = decodedArray[0];
      authPassword = decodedArray[1];

      /* 3. perform necessary action with the service handler... */
      HashMap signInResponse = customerService.signIn(authUserName, authPassword);

      /* 4. build the required response and send it back to the client side */
      /* header */
      HttpHeaders headers = new HttpHeaders();
      headers.add("access-token", (String) signInResponse.get("jwt"));
      /* login response */
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setId((String) signInResponse.get("id"));
      loginResponse.setMessage((String) signInResponse.get("message"));
      loginResponse.setFirstName((String) signInResponse.get("firstname"));
      loginResponse.setLastName((String) signInResponse.get("lastname"));
      loginResponse.setEmailAddress((String) signInResponse.get("email"));
      loginResponse.setContactNumber((String) signInResponse.get("contact"));
      /* response */
      return new ResponseEntity(loginResponse, headers, HttpStatus.OK);
    }
  }
}
