package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CouponDao {
    @PersistenceContext
    private EntityManager entityManager;


    //To get Coupon by Coupon Name from the db
    public CouponEntity getCouponByName(final String couponName) {
        try {
            return entityManager.createNamedQuery("getCouponByCouponName", CouponEntity.class)
                    .setParameter("couponName", couponName).getSingleResult();
        }catch(NoResultException nre) {
            return null;
        }
    }
}