package com.cuong.electronicstore.repository;

import com.cuong.electronicstore.model.Cart;
import com.cuong.electronicstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUser_Id(Long userId);
}
