package com.upgrad.FoodOrderingApp.api.controller;

/* spring imports */

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ItemList>> getRestaurantBy_uuid(@PathVariable("restaurant_id") final String restaurant_uuid) throws RestaurantNotFoundException {
        List<ItemEntity> itemEntities = itemService.getItemsByPopularity(restaurantService.restaurantByUUID(restaurant_uuid));
        List<ItemList> top5timeResponse = new ArrayList<>();
        System.out.println(itemEntities.size());
        for (ItemEntity entity : itemEntities) {
            System.out.println(entity.getItemName());
            ItemList itemList = new ItemList();
            itemList.setId(UUID.fromString(entity.getUuid()));
            itemList.setItemName(entity.getItemName());
            ItemList.ItemTypeEnum itemTypeEnum = null;
            try {
                itemTypeEnum = (Integer.valueOf(entity.getType().toString()) == 0)
                                ? ItemList.ItemTypeEnum.VEG
                                : ItemList.ItemTypeEnum.NON_VEG;
            } catch (NumberFormatException e) {
                String type = entity.getType().getValue();
                itemTypeEnum = ItemList.ItemTypeEnum.valueOf(type);
            }
            itemList.setItemType(itemTypeEnum);
            itemList.setPrice(entity.getPrice());
            top5timeResponse.add(itemList);
        }
        return new ResponseEntity<>(top5timeResponse, HttpStatus.OK);
    }
}
