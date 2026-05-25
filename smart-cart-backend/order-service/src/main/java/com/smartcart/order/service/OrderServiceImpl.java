package com.smartcart.order.service;

import com.smartcart.order.client.ProductClient;
import com.smartcart.order.dto.OrderRequestDTO;
import com.smartcart.order.dto.OrderResponseDTO;
import com.smartcart.order.dto.ProductResponseDTO;
import com.smartcart.order.entity.Order;
import com.smartcart.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    @Override
    @CircuitBreaker(name = "productService", fallbackMethod = "placeOrderFallback")
    public OrderResponseDTO placeOrder(OrderRequestDTO request) {
        log.info("Placing order for product ID: {}", request.getProductId());
        
        // Fetch product details via Feign Client
        ProductResponseDTO product = productClient.getProductById(request.getProductId());
        
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Optional logic: Check inventory
        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient product inventory");
        }
        
        // Deduct inventory
        productClient.reduceQuantity(request.getProductId(), request.getQuantity());

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        Order order = Order.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalAmount(totalAmount)
                .build();

        Order savedOrder = orderRepository.save(order);

        return mapToDTO(savedOrder);
    }

    public OrderResponseDTO placeOrderFallback(OrderRequestDTO request, Throwable throwable) {
        log.error("Fallback triggered due to: {}", throwable.getMessage());
        throw new RuntimeException("Product service is currently unavailable. Order cannot be placed.");
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO mapToDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .build();
    }
}
