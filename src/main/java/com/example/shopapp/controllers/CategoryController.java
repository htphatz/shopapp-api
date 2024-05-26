package com.example.shopapp.controllers;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.entities.Category;
import com.example.shopapp.responses.ArrayDataResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArrayDataResponse<Category> createCategory(@Valid CategoryDTO categoryDTO) {
        Category newCategory = categoryService.createCategory(categoryDTO);
        return new ArrayDataResponse<>(HttpStatus.OK.value(), "Create category successfully", List.of(newCategory));
    }

    @GetMapping
    public ResponseCustom<List<Category>> getCategories(
//            @RequestParam("page") int page,
//            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all categories successfully", categories);
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getCategoryById(@Valid @PathVariable("id") Long id) {
        Category existingCategory = categoryService.getCategoryById(id);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get category with id " + id + " successfully", existingCategory);
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateCategory(@Valid @PathVariable("id") Long id, @Valid CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update category with id " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteCategory(@Valid @PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete category with id " + id + " successfully");
    }
}
