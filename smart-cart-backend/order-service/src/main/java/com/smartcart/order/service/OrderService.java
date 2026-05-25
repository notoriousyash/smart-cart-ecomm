package com.smartcart.order.service;

import com.smartcart.order.dto.OrderRequestDTO;
import com.smartcart.order.dto.OrderResponseDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO placeOrder(OrderRequestDTO request);
    List<OrderResponseDTO> getOrdersByUserId(Long userId);
}
