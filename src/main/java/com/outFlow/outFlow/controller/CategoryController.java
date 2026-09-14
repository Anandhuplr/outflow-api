package com.outFlow.outFlow.controller;



import com.outFlow.outFlow.dto.CategoryRequest;
import com.outFlow.outFlow.entity.Category;
import com.outFlow.outFlow.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(
            CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getCategories(
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        return categoryService.getCategories(userId);
    }

    @PostMapping
    public Category createCategory(
            @RequestBody CategoryRequest request,
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        return categoryService.createCategory(
                userId,
                request.name()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        categoryService.deleteCategory(
                userId,
                id
        );
    }
}