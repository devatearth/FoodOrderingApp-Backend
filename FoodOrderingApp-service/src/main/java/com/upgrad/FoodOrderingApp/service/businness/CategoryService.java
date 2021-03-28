package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
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

    @Autowired
    private CategoryItemDao categoryItemDao;

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
    /* This method is to get All Categories Ordered By Name and returns list of CategoryEntity
       If error throws exception with error code and error message.
       */
    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        //Calls getAllCategoriesOrderedByName of categoryDao to get list of CategoryEntity
        List<CategoryEntity> categoryEntities = categoryDao.getAllCategoriesOrderedByName();
        return categoryEntities;
    }

    public List<CategoryEntity> getAllCategories() {

        //Calls all getCategories
        List<CategoryEntity> categoryEntities = restaurantCategoryDao.getAllCategories();
        return categoryEntities;
    }

    public CategoryEntity getCategoryById(String categoryUuid) throws CategoryNotFoundException {
        if (categoryUuid == null || categoryUuid == "") { //Checking for categoryUuid to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        }

        //Calls getCategoryByUuid of categoryDao to get CategoryEntity
        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUuid);

        if (categoryEntity == null) { //Checking for categoryEntity to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        return categoryEntity;
    }
}
