package com.outFlow.outFlow.service;


import com.outFlow.outFlow.entity.Category;
import com.outFlow.outFlow.entity.User;
import com.outFlow.outFlow.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public CategoryService(
            CategoryRepository categoryRepository,
            UserService userService) {

        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public List<Category> getCategories(Long userId) {

        return categoryRepository.findByUserId(userId);
    }

    public Category createCategory(
            Long userId,
            String name) {

        User user =
                userService.getUserById(userId);

        Category category = new Category();

        category.setUser(user);
        category.setName(name);
        category.setIsDefault(false);

        return categoryRepository.save(category);
    }

    public void deleteCategory(
            Long userId,
            Long categoryId) {

        Category category =
                categoryRepository
                        .findByIdAndUserId(
                                categoryId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                ));

        categoryRepository.delete(category);
    }
}