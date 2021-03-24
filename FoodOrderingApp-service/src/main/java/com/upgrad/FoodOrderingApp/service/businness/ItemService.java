package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private RestaurantDao restaurantDao;

    public List<ItemEntity> GetTop5Items(String restuarantUuiD) throws RestaurantNotFoundException {
       if(restaurantDao.restaurantByUUID(restuarantUuiD) == null){
           throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
       }
        return null;
    }
}
