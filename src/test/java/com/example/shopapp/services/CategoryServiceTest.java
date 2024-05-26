package com.example.shopapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryServiceTest {
    // Test
//    @Test
//    public void testCreateCategoryWithNoImage() {
//        Category category = new Category();
//        category.setImageUrl("assadiuasfnj");
//        CategoryDTO categoryDTO = new CategoryDTO();
//        categoryDTO.setName("test");
//        Category newCategory = categoryService.createCategory(categoryDTO);
//        Assertions.assertEquals(category.getImageUrl(), newCategory.getImageUrl());
//    }
    private CategoryService categoryService;

    @Value("${resource.category.noImageUrl}")
    private String noImageUrl;

    @Autowired
    public CategoryServiceTest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
