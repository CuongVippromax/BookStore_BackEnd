package com.cuong.electronicstore.service;

import com.cuong.electronicstore.dto.request.ProductRequest;
import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.dto.response.ProductResponse;
import com.cuong.electronicstore.exception.BadRequestException;
import com.cuong.electronicstore.exception.ResourceNotFoundException;
import com.cuong.electronicstore.model.Category;
import com.cuong.electronicstore.model.Product;
import com.cuong.electronicstore.repository.CategoryRepository;
import com.cuong.electronicstore.repository.ProductRepository;
import com.cuong.electronicstore.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    public ProductResponse addProduct(ProductRequest req) {
        if (productRepository.getProductByName(req.getName()).isPresent()) {
            throw new BadRequestException("Product already exists with name: " + req.getName());
        }
        Product product = Product.builder()
                .name(req.getName())
                .brand(req.getBrand())
                .description(req.getDescription())
                .sku(req.getSku())
                .model(req.getModel())
                .image(req.getImage())
                .price(req.getPrice())
                .stock(req.getStock())
                .soldQuantity(0)
                .warrantyMonths(req.getWarrantyMonths())
                .category(resolveCategory(req.getCategoryId()))
                .build();
        productRepository.save(product);
        return toResponse(product);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        if (StringUtils.hasLength(req.getName())) product.setName(req.getName());
        if (req.getBrand() != null) product.setBrand(req.getBrand());
        if (req.getDescription() != null) product.setDescription(req.getDescription());
        if (req.getSku() != null) product.setSku(req.getSku());
        if (req.getModel() != null) product.setModel(req.getModel());
        if (req.getImage() != null) product.setImage(req.getImage());
        if (req.getPrice() != null) product.setPrice(req.getPrice());
        if (req.getStock() != null) product.setStock(req.getStock());
        if (req.getWarrantyMonths() != null) product.setWarrantyMonths(req.getWarrantyMonths());
        if (req.getCategoryId() != null) product.setCategory(resolveCategory(req.getCategoryId()));
        productRepository.save(product);
        return toResponse(product);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    public PageResponse<List<ProductResponse>> findAllProduct(int page, int size, String keyword, String sort) {
        int pageNo = page > 0 ? page - 1 : 0;

        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                order = matcher.group(3).equalsIgnoreCase("asc")
                        ? new Sort.Order(Sort.Direction.ASC, columnName)
                        : new Sort.Order(Sort.Direction.DESC, columnName);
            }
        }
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));
        Page<Product> entityPage;
        if (StringUtils.hasLength(keyword)) {
            entityPage = productRepository.searchByKeyword("%" + keyword.toLowerCase() + "%", pageable);
        } else {
            entityPage = productRepository.findAll(pageable);
        }
        List<ProductResponse> list = entityPage.stream().map(this::toResponse).toList();
        return PageResponse.<List<ProductResponse>>builder()
                .page(page)
                .size(size)
                .totalPage(entityPage.getTotalPages())
                .totalElements(entityPage.getTotalElements())
                .data(list)
                .build();
    }

    private Category resolveCategory(Integer categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }

    public ProductResponse toResponse(Product product) {
        Double avg = reviewRepository.averageRatingByProduct(product.getId());
        Long total = reviewRepository.countByProduct(product.getId());
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .description(product.getDescription())
                .sku(product.getSku())
                .model(product.getModel())
                .image(product.getImage())
                .price(product.getPrice())
                .stock(product.getStock())
                .soldQuantity(product.getSoldQuantity())
                .warrantyMonths(product.getWarrantyMonths())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .averageRating(avg)
                .totalReviews(total)
                .build();
    }
}
