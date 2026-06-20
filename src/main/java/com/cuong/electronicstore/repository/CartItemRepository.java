package com.cuong.electronicstore.repository;

import com.cuong.electronicstore.model.Cart;
import com.cuong.electronicstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct_Id(Cart cart, Long productId);
    void deleteByCart(Cart cart);
}
