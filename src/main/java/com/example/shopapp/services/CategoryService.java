package com.example.shopapp.services;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        if (categoryDTO.getFileImage() == null) {
            String imageUrl = "https://res.cloudinary.com/drgidfvnd/image/upload/v1713243712/no-image.1024x1024_gyl3zk.png";
            newCategory.setImageUrl(imageUrl);
        } else {
            Map data = this.cloudinaryService.upload(categoryDTO.getFileImage());
            String imageUrl = (String) data.get("secure_url");
            newCategory.setImageUrl(imageUrl);
        }
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
        if (categoryDTO.getFileImage() == null) {
            String imageUrl = "https://res.cloudinary.com/drgidfvnd/image/upload/v1713243712/no-image.1024x1024_gyl3zk.png";
            existingCategory.setImageUrl(imageUrl);
        } else {
            Map data = this.cloudinaryService.upload(categoryDTO.getFileImage());
            String imageUrl = (String) data.get("secure_url");
            existingCategory.setImageUrl(imageUrl);
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
