package com.example.E_commerce.service.category;

import com.example.E_commerce.Entity.Category;

import java.util.List;

public interface ICategoryService {
    Category addCategory(Category category);
    Category updateCategory(Category category , Long categoryId);
    Category getCategoryById(Long categoryId);
    void deleteCategory(Long categoryId);
    List<Category> getAllCategories();
    List<Category> getCategoryByName(String name);
}
