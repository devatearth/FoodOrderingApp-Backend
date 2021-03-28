package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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
import java.util.UUID;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CategoryListResponse>> getCategories() {
        System.out.println("Invoked getCategories");
        List<CategoryEntity> categoryEntities = categoryService.getAllCategories();
        List<CategoryListResponse> allCategoryResponse = new ArrayList<>();
        CategoryEntity categoryEntitity;
        for (int i = 0; i < categoryEntities.size(); i++) {
            categoryEntitity = categoryEntities.get(i);
            CategoryListResponse categoryListResponse = new CategoryListResponse();
            categoryListResponse.setId(UUID.fromString(categoryEntitity.getUuid()));
            categoryListResponse.setCategoryName(categoryEntitity.getCategoryName());
            allCategoryResponse.add(categoryListResponse);
        }
        System.out.println(allCategoryResponse);
        return new ResponseEntity<List<CategoryListResponse>>(allCategoryResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoriesByID(@PathVariable(value = "category_id") final String categoryId) throws CategoryNotFoundException {
        List<CategoryItemEntity> categoryItemEntities = categoryService.getCategoryByID(categoryId);
        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        categoryItemEntities.forEach(category -> {
            categoryDetailsResponse.setId(UUID.fromString(categoryId));
            categoryDetailsResponse.setCategoryName(category.getCategoryId().getCategoryName());
            ItemList itemList = new ItemList();
            itemList.setItemName(category.getItemId().getItemName());
            itemList.setItemType((Integer.valueOf(category.getItemId().getType().toString()) == 0)
                    ? ItemList.ItemTypeEnum.VEG
                    : ItemList.ItemTypeEnum.NON_VEG);
            itemList.setId(category.getItemId().getUuid());
            itemList.setPrice(category.getItemId().getPrice());
            categoryDetailsResponse.addItemListItem(itemList);

        });
        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }
}
