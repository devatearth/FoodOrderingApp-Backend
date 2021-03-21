package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private CategoryDao categoryDao;

    /* This method is to get restaurants By Rating and returns list of RestaurantEntity
If error throws exception with error code and error message.
*/
    public List<RestaurantEntity> restaurantsByRating() {

        //Calls restaurantsByRating of restaurantDao to get list of RestaurantEntity
        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByRating();
        return restaurantEntities;
    }

    /* This method is to get restaurants By Name and returns list of RestaurantEntity. its takes restaurant name as the input string.
If error throws exception with error code and error message.
*/
    public List<RestaurantEntity> restaurantsByName(String restaurantName) throws RestaurantNotFoundException {
        if (restaurantName == null || restaurantName == "") { //Checking for restaunrant name to be null or empty to throw exception.
            throw new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty");
        }

        //Calls restaurantsByName of restaurantDao to get list of RestaurantEntity
        List<RestaurantEntity> restaurantEntities = restaurantDao.restaurantsByName(restaurantName);
        return restaurantEntities;
    }

}
