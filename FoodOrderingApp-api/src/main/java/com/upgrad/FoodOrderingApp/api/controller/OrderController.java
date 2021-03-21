package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    //getCouponByCouponName method to get the details of a coupon.
    @RequestMapping(
            path = "/order/coupon/{coupon_name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName(@RequestHeader("authorization") final String authorization,
                                                                       @PathVariable("coupon_name") final String couponName)
            throws AuthorizationFailedException, CouponNotFoundException {

        //Access the accessToken from the request Header
        if (authorization.indexOf("Bearer ") == -1) {
            throw new AuthorizationFailedException("ATH-004", "Bearer not found in the authorization header section");
        }
        else {
            //get the jwt token from the header
            String jwt = authorization.split("Bearer ")[1];

            //validate the access token in the header to proceed
            CustomerAuthEntity entity = customerService.validateAccessToken(jwt);

            //Calls getCouponByCouponName of orderService to get the coupon by name from DB
            CouponEntity couponEntity =  orderService.getCouponByCouponName(couponName);

            //Creating the couponDetailsResponse containing UUID,Coupon Name and percentage.
            CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
                    .id(UUID.fromString(couponEntity.getUuid()))
                    .couponName(couponEntity.getCouponName())
                    .percent(couponEntity.getPercent());

            return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);

        }
    }
}