package com.cuong.electronicstore.service;

import com.cuong.electronicstore.dto.request.ReviewRequest;
import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.dto.response.ReviewResponse;
import com.cuong.electronicstore.exception.BadRequestException;
import com.cuong.electronicstore.exception.ForbiddenException;
import com.cuong.electronicstore.exception.ResourceNotFoundException;
import com.cuong.electronicstore.model.Product;
import com.cuong.electronicstore.model.Review;
import com.cuong.electronicstore.model.User;
import com.cuong.electronicstore.repository.OrderItemRepository;
import com.cuong.electronicstore.repository.ProductRepository;
import com.cuong.electronicstore.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    public ReviewResponse create(ReviewRequest req) {
        User user = currentUserService.getCurrentUser();
        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + req.getProductId()));

        if (!orderItemRepository.existsByOrder_User_IdAndProduct_Id(user.getId(), product.getId())) {
            throw new BadRequestException("You can only review products you have purchased");
        }
        if (reviewRepository.existsByUser_IdAndProduct_Id(user.getId(), product.getId())) {
            throw new BadRequestException("You have already reviewed this product");
        }
        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(req.getRating())
                .comment(req.getComment())
                .build();
        reviewRepository.save(review);
        return toResponse(review);
    }

    @Transactional
    public ReviewResponse update(Long reviewId, ReviewRequest req) {
        User user = currentUserService.getCurrentUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));
        if (!review.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot edit other user's review");
        }
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        reviewRepository.save(review);
        return toResponse(review);
    }

    @Transactional
    public void delete(Long reviewId) {
        User user = currentUserService.getCurrentUser();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));
        if (!currentUserService.isCurrentUserAdmin() && !review.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot delete other user's review");
        }
        reviewRepository.delete(review);
    }

    public PageResponse<List<ReviewResponse>> getByProduct(Long productId, int page, int size) {
        int pageNo = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> result = reviewRepository.findByProduct_Id(productId, pageable);
        List<ReviewResponse> data = result.stream().map(this::toResponse).toList();
        return PageResponse.<List<ReviewResponse>>builder()
                .page(page)
                .size(size)
                .totalElements(result.getTotalElements())
                .totalPage(result.getTotalPages())
                .data(data)
                .build();
    }

    private ReviewResponse toResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .productId(r.getProduct().getId())
                .userId(r.getUser().getId())
                .username(r.getUser().getUsername())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
