package com.upgrad.FoodOrderingApp.service.dao;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/* project imports */
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;

/* java imports */
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Repository
public class CustomerAddressDao {
  @PersistenceContext
  private EntityManager entityManager;

  /* updates the customer auth entity in the 'custsomer_auth' table */
  public void createNewCustomerAddressEntry(CustomerAddressEntity customerAddressEntity) {
    this.entityManager.persist(customerAddressEntity);
  }

  /* fetches a single entity based on address id */
  public CustomerAddressEntity getEntityByAddressId(int addressId) {
    try {
      return this.entityManager.createNamedQuery("getEntityByAddressId", CustomerAddressEntity.class)
             .setParameter("address_id", addressId).getSingleResult();
    }
    catch (NoResultException nRE) {
      return null;
    }
  }
}
