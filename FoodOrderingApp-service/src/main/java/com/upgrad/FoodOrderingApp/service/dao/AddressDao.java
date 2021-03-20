package com.upgrad.FoodOrderingApp.service.dao;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/* project imports */
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;

/* java imports */
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.ArrayList;

@Repository
public class AddressDao {
  @PersistenceContext
  private EntityManager entityManager;

  /* fetches all the states in the db from the state table */
  public List<StateEntity> getAllStates() {
    List<StateEntity> listOfStates = entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
    return listOfStates;
  }

  /* fetches all the addresses saved in the address table */
  public List<AddressEntity> getAllAddresses() {
    List<AddressEntity> listOfAddresses = entityManager.createNamedQuery("selectAllAddresses", AddressEntity.class).getResultList();
    return listOfAddresses;
  }
  
  /* helps to fetch a single state entity */
  public StateEntity getStateByUuid(String uuid) {
    try {
      return entityManager.createNamedQuery("getStateByUuid", StateEntity.class).setParameter("uuid", uuid).getSingleResult();
    }
    catch (NoResultException nRE) {
      return null;
    }
  }

  /* helps to insert/create a new address entitty in the address table */
  public void createNewAddressEntity(AddressEntity newAddress) {
    this.entityManager.persist(newAddress);
  }
}
