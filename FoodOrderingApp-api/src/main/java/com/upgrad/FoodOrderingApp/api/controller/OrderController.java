package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ItemService itemService;

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
        } else {
            //get the jwt token from the header
            String jwt = authorization.split("Bearer ")[1];

            //validate the access token in the header to proceed
            CustomerAuthEntity entity = customerService.validateAccessToken(jwt);

            //Calls getCouponByCouponName of orderService to get the coupon by name from DB
            CouponEntity couponEntity = orderService.getCouponByCouponName(couponName);

            //Creating the couponDetailsResponse containing UUID,Coupon Name and percentage.
            CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
                    .id(UUID.fromString(couponEntity.getUuid()))
                    .couponName(couponEntity.getCouponName())
                    .percent(couponEntity.getPercent());

            return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse, HttpStatus.OK);

        }
    }

    //getPastOrdersOfUser method to get the Customer Past Order details
    @RequestMapping(
            method = RequestMethod.GET, path = "/order",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CustomerOrderResponse> getPastOrdersOfUser(@RequestHeader("authorization") final String authorization)
            throws AuthenticationFailedException, AuthorizationFailedException {
        //Access the accessToken from the request Header
        if (authorization.indexOf("Bearer ") == -1) {
            throw new AuthorizationFailedException("ATH-004", "Bearer not found in the authorization header section");
        } else {
            //get the jwt token from the header
            String jwt = authorization.split("Bearer ")[1];

            //validate the access token in the header to proceed
            CustomerAuthEntity entity = customerService.validateAccessToken(jwt);

            //Calls getOrdersByCustomers of orderService to get all the past orders of the customer.
            List<OrderEntity> ordersEntities = orderService.getOrdersByCustomers(entity.getUuid());

            //Creating List of OrderList
            List<OrderList> orderLists = new LinkedList<>();

            if (ordersEntities != null) { //Checking if orderentities is null if yes them empty list is returned
                for (OrderEntity orderEntity : ordersEntities) { //looping in for every orderentity in orderentities

                    //Calls getOrderItemsByOrder by order of orderService get all the items ordered in past by orders.
                    List<OrderItemEntity> orderItemEntities = orderService.getOrderItemsByOrder(orderEntity);

                    List<ItemQuantityResponse> itemQuantityResponseList = new LinkedList<>();
                    orderItemEntities.forEach(orderItemEntity -> {
                        ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem()
                        .itemName(orderItemEntity.getItem().getItemName())
                        .itemPrice(orderItemEntity.getItem().getPrice())
                        .id(UUID.fromString(String.valueOf(orderItemEntity.getItem().getUuid())))
                        .type(ItemQuantityResponseItem.TypeEnum.valueOf(orderItemEntity.getItem().getType()/*.getValue()*/));

                        //Creating ItemQuantityResponse which will be added to the list
                        ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse()
                                .item(itemQuantityResponseItem).quantity(orderItemEntity.getQuantity())
                                .price(orderItemEntity.getPrice());
                        itemQuantityResponseList.add(itemQuantityResponse);
                    });
                    OrderListAddressState orderListAddressState = new OrderListAddressState()
                            .id(UUID.fromString(orderEntity.getAddress().getState().getUuid()))
                            .stateName(orderEntity.getAddress().getState().getStateName());

                    OrderListAddress orderListAddress = new OrderListAddress().id(UUID.fromString(orderEntity.getAddress().getUuid()))
                            .flatBuildingName(orderEntity.getAddress().getFlatBuildingNumber())
                            .locality(orderEntity.getAddress().getLocality()).city(orderEntity.getAddress().getCity())
                            .pincode(orderEntity.getAddress().getPincode()).state(orderListAddressState);
                    OrderListCoupon orderListCoupon = new OrderListCoupon().couponName(orderEntity.getCoupon().getCouponName())
                            .id(UUID.fromString(orderEntity.getCoupon().getUuid())).percent(orderEntity.getCoupon().getPercent());

                    OrderListCustomer orderListCustomer = new OrderListCustomer()
                            .id(UUID.fromString(orderEntity.getCustomer().getUuid()))
                            .firstName(orderEntity.getCustomer().getFirstName())
                            .lastName(orderEntity.getCustomer().getLastName())
                            .emailAddress(orderEntity.getCustomer().getEmail())
                            .contactNumber(orderEntity.getCustomer().getContactNumber());

                    OrderList orderList = new OrderList().id(UUID.fromString(orderEntity.getUuid())).itemQuantities(itemQuantityResponseList)
                            .address(orderListAddress).bill(BigDecimal.valueOf(orderEntity.getBill()))
                            .date(String.valueOf(orderEntity.getDate())).discount(BigDecimal.valueOf(orderEntity.getDiscount()))
                            .coupon(orderListCoupon).customer(orderListCustomer);
                    orderLists.add(orderList);
                }
                CustomerOrderResponse customerOrderResponse = new CustomerOrderResponse()
                        .orders(orderLists);
                return new ResponseEntity<CustomerOrderResponse>(customerOrderResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<CustomerOrderResponse>(new CustomerOrderResponse(), HttpStatus.OK);
            }
        }
    }

    /* The method handles save Order request.It takes authorization from the header and other details in SaveOrderRequest.
        & produces response in SaveOrderResponse and returns UUID and successful message and if error returns error code and error Message.
        */
    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader("authorization") final String authorization,
                                                       @RequestBody(required = false) final SaveOrderRequest saveOrderRequest)
            throws AuthorizationFailedException, CouponNotFoundException, AddressNotFoundException,
            PaymentMethodNotFoundException, RestaurantNotFoundException, ItemNotFoundException {

        //Access the accessToken from the request Header
        if (authorization.indexOf("Bearer ") == -1) {
            throw new AuthorizationFailedException("ATH-004", "Bearer not found in the authorization header section");
        } else {
            //get the jwt token from the header
            String jwt = authorization.split("Bearer ")[1];

            //validate the access token in the header to proceed
            CustomerAuthEntity entity = customerService.validateAccessToken(jwt);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUuid(UUID.randomUUID().toString());
            orderEntity.setBill(saveOrderRequest.getBill().doubleValue());
            orderEntity.setDate(new Date());
            //orderEntity.setCustomer(entity);
            String couponUuid = saveOrderRequest.getCouponId().toString();
            if (couponUuid != null) {
                CouponEntity couponEntity = orderService.getCouponByCouponId(couponUuid);
                orderEntity.setCoupon(couponEntity);
            } else {
                orderEntity.setCoupon(null);
            }
            BigDecimal discount = saveOrderRequest.getDiscount();
            if (discount != null) {
                orderEntity.setDiscount(discount.doubleValue());
            } else {
                orderEntity.setDiscount(BigDecimal.ZERO.doubleValue());
            }

            String paymentUuid = saveOrderRequest.getPaymentId().toString();
            if (paymentUuid != null) {
                PaymentEntity paymentEntity = paymentService.getPaymentByUUID(paymentUuid);
                orderEntity.setPayment(paymentEntity);
            } else {
                orderEntity.setPayment(null);
            }

            String addressUuid = saveOrderRequest.getAddressId();

            /*AddressEntity addressEntity = addressService.getAddressByUuid(addressUuid, entity);
            orderEntity.setAddress(addressEntity);*/

            String restaurantUuid = saveOrderRequest.getRestaurantId().toString();

            RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantUuid);
            orderEntity.setRestaurant(restaurantEntity);

            List<ItemQuantity> itemList = saveOrderRequest.getItemQuantities();

            List<OrderItemEntity> orderItemEntityList = new ArrayList<>();

            OrderEntity savedOrderEntity = orderService.saveOrder(orderEntity);

            for(ItemQuantity itemQuantity: itemList) {
                OrderItemEntity orderedItem = new OrderItemEntity();
                /*ItemEntity itemEntity = itemService.getItemByUuid(itemQuantity.getItemId().toString());
                orderedItem.setItem(itemEntity);*/
                orderedItem.setOrder(savedOrderEntity);
                orderedItem.setQuantity(itemQuantity.getQuantity());
                orderedItem.setPrice(itemQuantity.getPrice());
                orderItemEntityList.add(orderedItem);
                orderService.saveOrderItem(orderedItem);
            }

            //Creating the SaveOrderResponse for the endpoint containing UUID and success message.
            SaveOrderResponse saveOrderResponse = new SaveOrderResponse().id(savedOrderEntity.getUuid())
                    .status("ORDER SUCCESSFULLY PLACED");
            return new ResponseEntity<SaveOrderResponse>(saveOrderResponse, HttpStatus.CREATED);
        }
    }
}