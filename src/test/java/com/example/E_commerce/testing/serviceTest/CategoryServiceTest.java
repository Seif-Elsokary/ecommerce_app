package com.example.E_commerce.testing.serviceTest;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Exceptions.AlreadyExistsFoundException;
import com.example.E_commerce.Exceptions.CategoryNotFoundException;
import com.example.E_commerce.repository.CategoryRepository;
import com.example.E_commerce.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp(){
        category = new Category();
        Long categoryId = 1L;
        category.setId(categoryId);
        category.setName("Electronics");
    }

    @Test
    void addCategory_ShouldReturnSuccessfully(){
        when(categoryRepository.existsByName(category.getName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.addCategory(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());

        verify(categoryRepository , times(1)).save(category);


    }

    @Test
    void addCategory_ShouldThrowException_WhenCategoryAlreadyExists() {
        when(categoryRepository.existsByName(category.getName())).thenReturn(true);

        assertThrows(AlreadyExistsFoundException.class , ()-> categoryService.addCategory(category));
        verify(categoryRepository, never()).save(any(Category.class));

    }


    @Test
    void updateCategory_ShouldReturnSuccessfully(){
        Category category = new Category("Updated Category Successfully!");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(category , 1L);

        assertNotNull(result);
        assertEquals("Updated Category Successfully!", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));

    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class , ()-> categoryService.updateCategory(category , 2L));
    }


    @Test
    void  getCategoryById_ShouldReturnSuccessfully(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category result = categoryService.getCategoryById(1L);
        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }


    @Test
    void deleteCategoryById_ShouldReturnSuccessfully(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void deleteCategoryById_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class , ()-> categoryService.deleteCategory(2L));
    }

    @Test
    void getAllCategories_ShouldReturnSuccessfully(){
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.getAllCategories();
        assertNotNull(result);
        assertEquals("Electronics", result.get(0).getName());
        assertEquals( 1, result.size());
    }

    @Test
    void getCategoryByName_ShouldReturnSuccessfully(){
        when(categoryRepository.findCategoryByName("Electronics")).thenReturn(List.of(category));

        List<Category> getCategoryByName = categoryService.getCategoryByName("Electronics");
        assertNotNull(getCategoryByName);
        assertEquals("Electronics", getCategoryByName.get(0).getName());
        assertEquals( 1, getCategoryByName.size());

    }
}
