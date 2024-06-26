package com.example.shopapp.services;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.entities.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategories(); // Khong can phan trang
    Category updateCategory(long id, CategoryDTO categoryDTO);
    void deleteCategory(long id);
}
