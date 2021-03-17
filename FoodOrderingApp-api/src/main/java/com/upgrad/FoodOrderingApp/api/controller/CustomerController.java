package com.upgrad.FoodOrderingApp.api.controller;

/* spring imports */
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/* java imports */

/* project imports */
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;

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
}
