package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
<<<<<<< HEAD
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
=======
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
>>>>>>> fe3f7b42bd14fb5fc6d11897f731c334285183e7
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
=======
import java.util.List;

>>>>>>> fe3f7b42bd14fb5fc6d11897f731c334285183e7
@Service
public class OrderService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    CouponDao couponDao;

<<<<<<< HEAD
=======
    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

>>>>>>> fe3f7b42bd14fb5fc6d11897f731c334285183e7
    //getCouponByCouponName API to fetch coupon details from coupon table.
    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByCouponName(final String couponName) throws AuthorizationFailedException,
            CouponNotFoundException {
        if(couponName == null || couponName == "" || couponName.isEmpty()) {
            throw new CouponNotFoundException("CPF-002", "Coupon name field should not be empty");
        }
        CouponEntity couponEntity = couponDao.getCouponByName(couponName);
        if(couponEntity == null){
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }
        return couponEntity;
    }

<<<<<<< HEAD
=======
    //get Orders By customerUUID
    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderEntity> getOrdersByCustomers(String customerUuid){

        CustomerEntity customerEntity = customerDao.getCustomerByUuid(customerUuid);

        List<OrderEntity> ordersEntities = orderDao.getOrdersByCustomers(customerEntity);
        return ordersEntities;
    }

    //Get Order Items By Order
    public List<OrderItemEntity> getOrderItemsByOrder(OrderEntity orderEntity) {
        List<OrderItemEntity> orderItemEntities = orderItemDao.getOrderItemsByOrder(orderEntity);
        return orderItemEntities;
    }

>>>>>>> fe3f7b42bd14fb5fc6d11897f731c334285183e7
}