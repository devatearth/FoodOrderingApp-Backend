package com.upgrad.FoodOrderingApp.service.businness;

/* spring imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* project imports */
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;

/* java imports */
import java.util.List;
import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AddressService {
  @Autowired
  AddressDao addressDao;

  @Autowired
  ServiceUtility serviceUtility;

  /* connects with the dao to fetch all states from the db */
  public List<StateEntity> getAllStates() {
    return addressDao.getAllStates();
  }

  /* helps to fetch all the address through the address dao handler */
  public List<AddressEntity> getAllAddresses() {
    return addressDao.getAllAddresses();
  }

  /* validates the details of the address being sent and if ok, will help insert them in the db */
  @Transactional
  public String createAddressIfValid(String building, String locality, String city, String pincode, String stateUuid) 
  throws SaveAddressException, Exception {
    if (
      serviceUtility.isStringNullOrEmpty(building) ||
      serviceUtility.isStringNullOrEmpty(locality) ||
      serviceUtility.isStringNullOrEmpty(city) ||
      serviceUtility.isStringNullOrEmpty(pincode) ||
      serviceUtility.isStringNullOrEmpty(stateUuid)
    ) {
      throw new SaveAddressException("SAR-001", "No field can be empty");
    }
    else if (!serviceUtility.isValidPincode(pincode)) {
      throw new SaveAddressException("SAR-002", "Invalid pincode");
    }
    else {
      StateEntity state = addressDao.getStateByUuid(stateUuid);
      if (state == null) {
        throw new AddressNotFoundException("ANF-002", "No state by this id");
      }
      else {
        /* if you are here, then the validation process is completed, we can now create a new instance 
           in the db for an address */
        String addressUuid = UUID.randomUUID().toString();
        AddressEntity newAddress = new AddressEntity();

        /* set */
        newAddress.setFlatBuildingNumber(building);
        newAddress.setLocality(locality);
        newAddress.setCity(city);
        newAddress.setPincode(pincode);
        newAddress.setUuid(addressUuid);
        newAddress.setState(state);

        /* make the request */
        addressDao.createNewAddressEntity(newAddress);

        /* return value to the controller */
        return addressUuid;
      }
    }
  }
}
