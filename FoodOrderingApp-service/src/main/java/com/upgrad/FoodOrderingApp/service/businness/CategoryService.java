package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CategoryService {


    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao; //Handles all data related to the RestaurantCategoryEntity


    @Autowired
    private RestaurantDao restaurantDao; //Handles all data related to the RestaurantEntity

    @Autowired
    private CategoryDao categoryDao;

    public List<CategoryEntity> getCategoriesByRestaurant(String restaurantUuid) {

        //Calls getRestaurantByUuid of restaurantDao to get RestaurantEntity
        RestaurantEntity restaurantEntity = restaurantDao.restaurantByUUID(restaurantUuid);

        //Calls getCategoriesByRestaurant of restaurantCategoryDao to get list of RestaurantCategoryEntity
        List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantCategoryDao.getCategoriesByRestaurant(restaurantEntity);

        //Creating the list of the Category entity to be returned.
        List<CategoryEntity> categoryEntities = new LinkedList<>();
        restaurantCategoryEntities.forEach(restaurantCategoryEntity -> {
            categoryEntities.add(restaurantCategoryEntity.getCategoryId());
        });
        return categoryEntities;
    }

}
