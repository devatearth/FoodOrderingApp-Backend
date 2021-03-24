package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
}
