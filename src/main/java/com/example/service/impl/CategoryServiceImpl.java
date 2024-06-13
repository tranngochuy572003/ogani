package com.example.service.impl;

import com.example.Utils.StringUtils;
import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.exception.BadRequestException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@Data
@AllArgsConstructor

public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryRepository categoryRepository ;

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public List<Category> findByType(String type) {
    if(StringUtils.containsSpecialCharacters(type)){
      throw new BadRequestException("Type is invalid");
    }
    return categoryRepository.findByType(type);
  }

  @Override
  public void addCategory(CategoryDto categoryDto) {
    if(StringUtils.containsSpecialCharacters(categoryDto.getType())){
      throw new BadRequestException("Type is invalid");
    }
    if(StringUtils.containsSpecialCharacters(categoryDto.getName())){
      throw new BadRequestException("Name is invalid");
    }
    Category category = CategoryMapper.toEntity(categoryDto);
    categoryRepository.save(category);
  }


}
