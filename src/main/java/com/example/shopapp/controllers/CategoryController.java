package com.example.shopapp.controllers;

import com.example.shopapp.components.LocalizationUtils;
import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Category;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCustom<?> createCategory(
            @Valid CategoryDTO categoryDTO,
            BindingResult result)
    {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Create category failed");
        }
        Category newCategory = categoryService.createCategory(categoryDTO);
        List<Category> categories = new ArrayList<Category>();
        categories.add(newCategory);
        return new ResponseCustom<>(HttpStatus.CREATED.value(), "Create category successfully", categories);
    }

    @GetMapping
    public ResponseCustom<List<Category>> getCategories(
//            @RequestParam("page") int page,
//            @RequestParam("limit") int limit
    ) {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get all categories successfully", categories);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get all categories");
        }
    }

    @GetMapping("/{id}")
    public ResponseCustom<?> getCategoryById(@PathVariable("id") Long id) {
        try {
            Category existingCategory = categoryService.getCategoryById(id);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get category with id " + id + " successfully", existingCategory);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get category with id " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateCategory(
            @PathVariable("id") Long id,
            CategoryDTO categoryDTO) throws DataNotFoundException {
        try {
            categoryService.updateCategory(id, categoryDTO);
            return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update category with id " + id + " successfully");
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(),"Cannot update category with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete category with id " + id + " successfully");
    }
}
