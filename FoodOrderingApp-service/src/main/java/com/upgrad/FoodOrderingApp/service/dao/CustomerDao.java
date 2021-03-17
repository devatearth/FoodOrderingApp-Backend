package com.upgrad.FoodOrderingApp.service.dao;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/* project imports */
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

/* java imports */
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
public class CustomerDao {
  @PersistenceContext
  private EntityManager entityManager;

  /* checks to see if a customer entity is there based on contact number */
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

  /* creates a new customer instance in the db */
  public void registerNewCustomer(CustomerEntity newCustomer) {
    System.out.println(">_ registering new user in the database...");
    this.entityManager.persist(newCustomer);
  }
}
