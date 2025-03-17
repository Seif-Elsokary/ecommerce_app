package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.Category;
import com.example.E_commerce.Exceptions.CategoryNotFoundException;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category category1 = categoryService.addCategory(category);
            ApiResponse apiResponse = new ApiResponse("Add Category Successfully", category1);
            return ResponseEntity.ok(apiResponse);
        }catch (CategoryNotFoundException c){
            ApiResponse apiResponse = new ApiResponse("Add Category Failed", c.getMessage());
            return ResponseEntity.status(CONFLICT).body(apiResponse);
        }
    }

    @PutMapping("/{categoryId}/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody Category category , @PathVariable Long categoryId) {
        try {
            Category category1 = categoryService.getCategoryById(categoryId);
            if (category1 != null) {
                categoryService.updateCategory(category , categoryId);
                ApiResponse apiResponse = new ApiResponse("Update Category Successfully", category);
                return ResponseEntity.ok(apiResponse);
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("update failed!" , null));
        }catch (CategoryNotFoundException c){
            ApiResponse apiResponse = new ApiResponse("Update Category Failed", c.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category1 = categoryService.getCategoryById(categoryId);
            if (category1 != null) {
                ApiResponse apiResponse = new ApiResponse("Get Category Successfully", category1);
                return ResponseEntity.ok(apiResponse);
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("get failed!" , null));
        }catch (CategoryNotFoundException c){
            ApiResponse apiResponse = new ApiResponse("Update Category Failed", c.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @DeleteMapping("/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
        try {
            Category category1 = categoryService.getCategoryById(categoryId);
            if (category1 != null) {
                categoryService.deleteCategory(categoryId);
                ApiResponse apiResponse = new ApiResponse("delete Category Successfully", null);
                return ResponseEntity.ok(apiResponse);
            }
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("deleted failed!" , null));
        }catch (CategoryNotFoundException c){
            ApiResponse apiResponse = new ApiResponse("Update Category Failed", c.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            ApiResponse apiResponse = new ApiResponse("Get Categories Successfully", categories);
            return ResponseEntity.ok(apiResponse);
        }catch (CategoryNotFoundException c){
            ApiResponse apiResponse = new ApiResponse("get Category Failed", c.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(apiResponse);
        }
    }

    @GetMapping("/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            List<Category> category = categoryService.getCategoryByName(name);
            ApiResponse apiResponse = new ApiResponse("get Category Successfully", category);
            return ResponseEntity.ok(apiResponse);
        }catch (CategoryNotFoundException c){
            ApiResponse apiResponse = new ApiResponse("get Category Failed", c.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
