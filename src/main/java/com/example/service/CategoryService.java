package com.example.service;

import com.example.dto.CategoryDto;
import com.example.entity.Category;

import java.util.List;

public interface CategoryService {
  List<Category> getAllCategories();

  List<Category> findByType (String type) ;

  void addCategory(CategoryDto categoryDto);



}
