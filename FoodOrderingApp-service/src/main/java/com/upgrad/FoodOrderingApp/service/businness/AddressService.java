package com.upgrad.FoodOrderingApp.service.businness;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* project imports */
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;

/* java imports */
import java.util.List;

@Service
public class AddressService {
  @Autowired
  AddressDao addressDao;

  /* connects with the dao to fetch all states from the db */
  public List<StateEntity> getAllStates() {
    return addressDao.getAllStates();
  }
}
