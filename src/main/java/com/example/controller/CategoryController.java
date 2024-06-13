package com.example.controller;

import com.example.dto.CategoryDto;
import com.example.dto.UserDtoLogin;
import com.example.entity.Category;

import com.example.securityconfig.AuthService;
import com.example.securityconfig.JwtTokenProvider;
import com.example.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.common.ITEM_CREATED_SUCCESS;

@RestController
@RequestMapping("/api/v1/categories")

public class CategoryController {
  @Autowired
  private  CategoryServiceImpl categoryServiceImpl;

  @GetMapping("/getCategoriesByType/{type}")
  public ResponseEntity<?> getCategoriesByType(@PathVariable String type){
    List<Category> categories = categoryServiceImpl.findByType(type);
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @GetMapping("/getAllCategories")
  public ResponseEntity<?> getAllCategories (){
    List<Category> categories = categoryServiceImpl.getAllCategories();
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto){
    categoryServiceImpl.addCategory(categoryDto);
    return new ResponseEntity<>(ITEM_CREATED_SUCCESS, HttpStatus.OK);
  }
}
