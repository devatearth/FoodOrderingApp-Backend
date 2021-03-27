package com.upgrad.FoodOrderingApp.api.controller;

/* spring imports */

import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.ItemService;
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

    @RequestMapping(method = RequestMethod.GET, path = "/item/restaurant/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ItemList>> getRestaurantBy_uuid(@PathVariable("restaurant_id") final String restaurant_uuid) throws RestaurantNotFoundException {
        List<ItemEntity> itemEntities = itemService.GetTop5Items(restaurant_uuid);
        ItemEntity itemEntity;
        List<ItemList> top5timeResponse = new ArrayList<>();
        for (int i = 0; i < itemEntities.size(); i++) {
            itemEntity = itemEntities.get(i);
            ItemList item = new ItemList();
            item.setId(itemEntity.getUuid());
            item.setItemName(itemEntity.getItemName());
            top5timeResponse.add(item);
        }
        return new ResponseEntity<>(top5timeResponse, HttpStatus.OK);
    }
}
