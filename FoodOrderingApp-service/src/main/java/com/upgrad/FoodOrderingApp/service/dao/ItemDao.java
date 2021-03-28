package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;


@Repository
public class ItemDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ItemEntity> getOrdersByRestaurant(RestaurantEntity restaurant) {
        List<ItemEntity> items =
                entityManager
                        .createNamedQuery("topFivePopularItemsByRestaurant", ItemEntity.class)
                        .setParameter(0, restaurant.getId())
                        .getResultList();
        if (items != null) {
            System.out.println("returned list");
            return items;
        }
        System.out.println("returned empty list");
        return Collections.emptyList();
    }
}
