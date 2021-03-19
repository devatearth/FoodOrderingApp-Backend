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
