package com.upgrad.FoodOrderingApp.api.controller;

/* spring imports */

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {


    @RequestMapping(method = RequestMethod.GET, path = "/question/edit/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getRestaurantBy_uuid(@PathVariable("restaurant_id") final String restaurant_uuid){
       return "Hello";
    }
}
