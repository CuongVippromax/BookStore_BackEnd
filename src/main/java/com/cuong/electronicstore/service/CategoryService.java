package com.cuong.electronicstore.service;

import com.cuong.electronicstore.dto.request.CategoryRequest;
import com.cuong.electronicstore.dto.response.CategoryResponse;
import com.cuong.electronicstore.exception.BadRequestException;
import com.cuong.electronicstore.exception.ResourceNotFoundException;
import com.cuong.electronicstore.model.Category;
import com.cuong.electronicstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse create(CategoryRequest req) {
        if (categoryRepository.existsByName(req.getName())) {
            throw new BadRequestException("Category already exists: " + req.getName());
        }
        Category category = Category.builder()
                .name(req.getName())
                .description(req.getDescription())
                .build();
        categoryRepository.save(category);
        return toResponse(category);
    }

    public CategoryResponse update(Integer id, CategoryRequest req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        categoryRepository.save(category);
        return toResponse(category);
    }

    public void delete(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponse getById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return toResponse(category);
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    private CategoryResponse toResponse(Category c) {
        return CategoryResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .build();
    }
}
