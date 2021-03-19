package com.upgrad.FoodOrderingApp.service.dao;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/* project imports */
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;

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

  public List<StateEntity> getAllStates() {
    List<StateEntity> listOfStates = entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
    return listOfStates;
  }
}
