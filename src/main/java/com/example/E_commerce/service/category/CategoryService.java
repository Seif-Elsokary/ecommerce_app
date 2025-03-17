package com.example.E_commerce.service.category;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Exceptions.AlreadyExistsFoundException;
import com.example.E_commerce.Exceptions.CategoryNotFoundException;
import com.example.E_commerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category)
                .filter(c-> !categoryRepository.existsByName(category.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistsFoundException(category.getName() + " Already Exists!, you may updated it instead of!"));
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        return Optional.ofNullable(getCategoryById(categoryId))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(()-> new CategoryNotFoundException("category not found!"));
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new CategoryNotFoundException("category with id: " + categoryId + " Not Found!" ));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new CategoryNotFoundException("category with id: " + categoryId + " Not Found!");
                });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }
}
