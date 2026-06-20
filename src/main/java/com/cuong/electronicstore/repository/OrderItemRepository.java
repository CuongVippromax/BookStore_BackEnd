package com.cuong.electronicstore.repository;

import com.cuong.electronicstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByOrder_User_IdAndProduct_Id(Long userId, Long productId);
}
