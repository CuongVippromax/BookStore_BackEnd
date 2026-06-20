package com.cuong.electronicstore.service;

import com.cuong.electronicstore.dto.request.AddToCartRequest;
import com.cuong.electronicstore.dto.request.UpdateCartItemRequest;
import com.cuong.electronicstore.dto.response.CartItemResponse;
import com.cuong.electronicstore.dto.response.CartResponse;
import com.cuong.electronicstore.exception.BadRequestException;
import com.cuong.electronicstore.exception.ResourceNotFoundException;
import com.cuong.electronicstore.model.Cart;
import com.cuong.electronicstore.model.CartItem;
import com.cuong.electronicstore.model.Product;
import com.cuong.electronicstore.model.User;
import com.cuong.electronicstore.repository.CartItemRepository;
import com.cuong.electronicstore.repository.CartRepository;
import com.cuong.electronicstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public CartResponse getMyCart() {
        Cart cart = getOrCreateCart(currentUserService.getCurrentUser());
        return toResponse(cart);
    }

    @Transactional
    public CartResponse addItem(AddToCartRequest req) {
        User user = currentUserService.getCurrentUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + req.getProductId()));

        CartItem item = cartItemRepository.findByCartAndProduct_Id(cart, product.getId())
                .orElse(null);
        int newQty = (item != null ? item.getQuantity() : 0) + req.getQuantity();
        if (newQty > product.getStock()) {
            throw new BadRequestException("Not enough stock for product: " + product.getName());
        }
        if (item == null) {
            item = CartItem.builder().cart(cart).product(product).quantity(req.getQuantity()).build();
        } else {
            item.setQuantity(newQty);
        }
        cartItemRepository.save(item);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(Long itemId, UpdateCartItemRequest req) {
        User user = currentUserService.getCurrentUser();
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + itemId));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to current user");
        }
        if (req.getQuantity() > item.getProduct().getStock()) {
            throw new BadRequestException("Not enough stock for product: " + item.getProduct().getName());
        }
        item.setQuantity(req.getQuantity());
        cartItemRepository.save(item);
        return toResponse(cart);
    }

    @Transactional
    public CartResponse removeItem(Long itemId) {
        User user = currentUserService.getCurrentUser();
        Cart cart = getOrCreateCart(user);
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + itemId));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to current user");
        }
        cartItemRepository.delete(item);
        cart.getItems().removeIf(i -> i.getId().equals(itemId));
        return toResponse(cart);
    }

    @Transactional
    public void clear() {
        User user = currentUserService.getCurrentUser();
        Cart cart = getOrCreateCart(user);
        cartItemRepository.deleteByCart(cart);
        cart.getItems().clear();
    }

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
    }

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream().map(it -> {
            BigDecimal price = it.getProduct().getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(it.getQuantity()));
            return CartItemResponse.builder()
                    .id(it.getId())
                    .productId(it.getProduct().getId())
                    .productName(it.getProduct().getName())
                    .productImage(it.getProduct().getImage())
                    .price(price)
                    .quantity(it.getQuantity())
                    .subtotal(subtotal)
                    .build();
        }).toList();
        BigDecimal total = items.stream().map(CartItemResponse::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalItems = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .totalItems(totalItems)
                .totalAmount(total)
                .build();
    }
}
