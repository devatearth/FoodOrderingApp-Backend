package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager entityManager;

    //Get Orders By Customers
    public List<OrderEntity> getOrdersByCustomers(CustomerEntity customerEntity) {
        try {
            List<OrderEntity> ordersEntities = entityManager.createNamedQuery("getOrdersByCustomer", OrderEntity.class).setParameter("customer", customerEntity).getResultList();
            return ordersEntities;
        } catch (NoResultException nre){
            return null;
        }
    }

}
