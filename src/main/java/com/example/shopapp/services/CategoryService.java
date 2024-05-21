package com.example.shopapp.services;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Value("${resource.noImageUrl}")
    private String noImageUrl;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        String imageUrl = noImageUrl;
        // Nếu tồn tại file thì upload lên Cloudinary
        if (categoryDTO.getFileImage() != null && !categoryDTO.getFileImage().isEmpty()) {
            Map data = this.cloudinaryService.upload(categoryDTO.getFileImage());
            imageUrl = (String) data.get("secure_url");
        }
        newCategory.setImageUrl(imageUrl);
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot found category with id: " + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(long id, CategoryDTO categoryDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category with id " + id + " not found"));
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setImageUrl(existingCategory.getImageUrl());
        if (categoryDTO.getFileImage() != null && !categoryDTO.getFileImage().isEmpty()) {
            Map data = this.cloudinaryService.upload(categoryDTO.getFileImage());
            String newImageUrl = (String) data.get("secure_url");
            existingCategory.setImageUrl(newImageUrl);
        }
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}
