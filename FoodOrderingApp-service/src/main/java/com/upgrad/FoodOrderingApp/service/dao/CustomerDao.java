package com.upgrad.FoodOrderingApp.service.dao;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/* project imports */
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;

/* java imports */
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.ZonedDateTime;

@Repository
public class CustomerDao {
  @PersistenceContext
  private EntityManager entityManager;

  /* checks to see if a customer entity is there based on contact number and returns it*/
  public CustomerEntity getUserByContactNumber(String contactNumber) {
    System.out.println(">_ checking to see if user is already existing or not...");
    try {
      return this.entityManager.createNamedQuery("getUserByContactNumber", CustomerEntity.class)
             .setParameter("contactNumber", contactNumber).getSingleResult();
    }
    catch (NoResultException nRE) {
      return null;
    }
  } 

  /* checks to see if a customer entity is there based on a customer id and returns it */
  public CustomerEntity getCustomerEntityById(int customerId) {
    System.out.println(">_ checking to see if user is already existing or not...");
    try {
      return this.entityManager.createNamedQuery("getUserByCustomerId", CustomerEntity.class)
             .setParameter("id", customerId).getSingleResult();
    }
    catch (NoResultException nRE) {
      return null;
    }
  }

  /* checks to see if a customer auth entity is there based on an access token */
  public CustomerAuthEntity getCustomerEntityByAccessToken(String jwt) {
    System.out.println(">_ checking to see if customer auth entity is there or not by token id...");
    try {
      return this.entityManager.createNamedQuery("getEntityByToken", CustomerAuthEntity.class)
             .setParameter("accessToken", jwt).getSingleResult();
    }
    catch (NoResultException nRE) {
      return null;
    }
  }

  /* creates a new customer instance in the db */
  public void registerNewCustomer(CustomerEntity newCustomer) {
    System.out.println(">_ registering new user in the database...");
    this.entityManager.persist(newCustomer);
  }

  /* creates a registry for login in the db */
  public void registerLoginSession(CustomerEntity customer, String jwt, ZonedDateTime now, ZonedDateTime expiresAt) {
    CustomerAuthEntity authEntity = new CustomerAuthEntity();
    authEntity.setExpiresAt(expiresAt);
    authEntity.setCustomerId(customer.getId());
    authEntity.setLoginAt(now);
    authEntity.setAccessToken(jwt);
    authEntity.setUuid(customer.getUuid());
    this.entityManager.persist(authEntity);
  }

  /* updates the customer auth entity in the 'custsomer_auth' table */
  public void updateCustomerAuthEntity(CustomerAuthEntity authEntity) {
    entityManager.merge(authEntity);
  }

  /* updates the customer entity in the 'customer' table */
  public void updateCustomerEntity(CustomerEntity customer) {
    entityManager.merge(customer);
  }
}
