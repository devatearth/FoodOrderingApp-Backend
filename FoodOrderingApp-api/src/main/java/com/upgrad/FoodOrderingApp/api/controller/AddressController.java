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

/* project imports */
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.api.model.StatesList;
import com.upgrad.FoodOrderingApp.api.model.StatesListResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;

/* java imports */
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@RestController
public class AddressController {
  @Autowired
  CustomerService customerService;

  @Autowired
  AddressService addressService;

  /* @CrossOrigin(origins = "http://localhost:8080") */
  @RequestMapping (
    path = "/address", 
    method = RequestMethod.POST, 
    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity<SaveAddressResponse> saveAddress(
    @RequestHeader("authorization") String authorization, @RequestBody SaveAddressRequest addressRequest
  ) throws AuthorizationFailedException, SaveAddressException, Exception {
    /* 1. lets check if the authorization header is in a proper format and then proceed, otherwise throw error */
    if (authorization.indexOf("Bearer ") == -1) {
      throw new AuthorizationFailedException("ATH-004", "Bearer not found in the authorizaton header section");
    }
    else {
      /* 2. get the jwt token from the header */
      String jwt = authorization.split("Bearer ")[1];

      /* 3. validate the access token in the header to proceed */
      CustomerAuthEntity entity = customerService.validateAccessToken(jwt); 

      /* 4. validate and create an entry in the database with the service if applicable */
      String newAddressUuid = addressService.createAddressIfValid(
        addressRequest.getFlatBuildingName(), addressRequest.getLocality(), addressRequest.getCity(), 
        addressRequest.getPincode(), addressRequest.getStateUuid()
      );

      /* finally send the response to the client side */
      SaveAddressResponse saveAddressResponse = new SaveAddressResponse();
      saveAddressResponse.setId(newAddressUuid);
      saveAddressResponse.setStatus("ADDRESS SUCCESSFULLY SAVED");
      return new ResponseEntity(saveAddressResponse, HttpStatus.OK);
    }
  }

  /* @CrossOrigin(origins = "http://localhost:8080") */
  @RequestMapping (
    path = "/states", 
    method = RequestMethod.GET, 
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public ResponseEntity<StatesListResponse> getAllStates() {
    /* 1. invoke the method in the address service */
    List<StateEntity> states = addressService.getAllStates();

    /* 2. create a final list that would be used, iterate over the states list from previous line */
    List<StatesList> list = new ArrayList<StatesList>();
    for (StateEntity state : states) {
      StatesList listItem = new StatesList();
      listItem.setId(UUID.fromString(state.getUuid()));
      listItem.setStateName(state.getStateName());
      list.add(listItem);
    }

    /* 3. finally send the details to the client side */
    StatesListResponse apiResponse = new StatesListResponse();
    apiResponse.setStates(list);
    return new ResponseEntity(apiResponse, HttpStatus.OK);
  }
}
