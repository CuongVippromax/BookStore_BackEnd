package com.cuong.electronicstore.service;

import com.cuong.electronicstore.common.OrderStatus;
import com.cuong.electronicstore.common.PaymentMethod;
import com.cuong.electronicstore.common.PaymentStatus;
import com.cuong.electronicstore.dto.request.CreateOrderRequest;
import com.cuong.electronicstore.dto.response.OrderItemResponse;
import com.cuong.electronicstore.dto.response.OrderResponse;
import com.cuong.electronicstore.dto.response.PageResponse;
import com.cuong.electronicstore.dto.response.PaymentResponse;
import com.cuong.electronicstore.exception.BadRequestException;
import com.cuong.electronicstore.exception.ForbiddenException;
import com.cuong.electronicstore.exception.ResourceNotFoundException;
import com.cuong.electronicstore.model.*;
import com.cuong.electronicstore.repository.OrderRepository;
import com.cuong.electronicstore.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final CurrentUserService currentUserService;

    @Transactional
    public OrderResponse checkout(CreateOrderRequest req) {
        User user = currentUserService.getCurrentUser();
        Cart cart = cartService.getOrCreateCart(user);
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = Order.builder()
                .orderCode("OD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .receiverName(req.getReceiverName())
                .receiverPhone(req.getReceiverPhone())
                .shippingAddress(req.getShippingAddress())
                .note(req.getNote())
                .status(OrderStatus.PENDING)
                .build();

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            Product product = ci.getProduct();
            if (ci.getQuantity() > product.getStock()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - ci.getQuantity());
            product.setSoldQuantity((product.getSoldQuantity() == null ? 0 : product.getSoldQuantity()) + ci.getQuantity());
            productRepository.save(product);

            BigDecimal price = product.getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(subtotal);

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(ci.getQuantity())
                    .price(price)
                    .build();
            orderItems.add(oi);
        }
        order.setItems(orderItems);
        order.setTotalAmount(total);

        Payment payment = Payment.builder()
                .order(order)
                .method(PaymentMethod.COD)
                .status(PaymentStatus.PENDING)
                .amount(total)
                .build();
        order.setPayment(payment);

        orderRepository.save(order);
        cartService.clear();
        return toResponse(order);
    }

    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
        User user = currentUserService.getCurrentUser();
        if (!currentUserService.isCurrentUserAdmin() && !order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot access this order");
        }
        return toResponse(order);
    }

    public PageResponse<List<OrderResponse>> getMyOrders(int page, int size) {
        User user = currentUserService.getCurrentUser();
        int pageNo = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Order> result = orderRepository.findByUser_Id(user.getId(), pageable);
        return toPage(page, size, result);
    }

    public PageResponse<List<OrderResponse>> getAll(int page, int size, OrderStatus status) {
        int pageNo = page > 0 ? page - 1 : 0;
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Order> result = (status != null)
                ? orderRepository.findByStatus(status, pageable)
                : orderRepository.findAll(pageable);
        return toPage(page, size, result);
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot update status of a " + order.getStatus() + " order");
        }
        order.setStatus(newStatus);
        if (newStatus == OrderStatus.DELIVERED && order.getPayment() != null) {
            order.getPayment().setStatus(PaymentStatus.PAID);
            order.getPayment().setPaidAt(new java.util.Date());
        }
        orderRepository.save(order);
        return toResponse(order);
    }

    @Transactional
    public OrderResponse cancel(Long orderId) {
        User user = currentUserService.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if (!currentUserService.isCurrentUserAdmin() && !order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Cannot cancel this order");
        }
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BadRequestException("Order cannot be cancelled at status: " + order.getStatus());
        }
        for (OrderItem oi : order.getItems()) {
            Product product = oi.getProduct();
            product.setStock(product.getStock() + oi.getQuantity());
            product.setSoldQuantity(Math.max(0, (product.getSoldQuantity() == null ? 0 : product.getSoldQuantity()) - oi.getQuantity()));
            productRepository.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        if (order.getPayment() != null && order.getPayment().getStatus() == PaymentStatus.PAID) {
            order.getPayment().setStatus(PaymentStatus.REFUNDED);
        }
        orderRepository.save(order);
        return toResponse(order);
    }

    private PageResponse<List<OrderResponse>> toPage(int page, int size, Page<Order> result) {
        List<OrderResponse> data = result.stream().map(this::toResponse).toList();
        return PageResponse.<List<OrderResponse>>builder()
                .page(page)
                .size(size)
                .totalElements(result.getTotalElements())
                .totalPage(result.getTotalPages())
                .data(data)
                .build();
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(it ->
                OrderItemResponse.builder()
                        .id(it.getId())
                        .productId(it.getProduct().getId())
                        .productName(it.getProduct().getName())
                        .productImage(it.getProduct().getImage())
                        .quantity(it.getQuantity())
                        .price(it.getPrice())
                        .subtotal(it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())))
                        .build()
        ).toList();
        PaymentResponse paymentResp = null;
        if (order.getPayment() != null) {
            Payment p = order.getPayment();
            paymentResp = PaymentResponse.builder()
                    .id(p.getId())
                    .orderId(order.getId())
                    .method(p.getMethod())
                    .status(p.getStatus())
                    .amount(p.getAmount())
                    .paidAt(p.getPaidAt())
                    .build();
        }
        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUser().getId())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .shippingAddress(order.getShippingAddress())
                .note(order.getNote())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(items)
                .payment(paymentResp)
                .createdAt(order.getCreatedAt())
                .build();
    }
}
