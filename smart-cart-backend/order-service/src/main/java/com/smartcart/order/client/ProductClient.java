package com.smartcart.order.client;

import com.smartcart.order.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product.service.url:http://localhost:8081}")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponseDTO getProductById(@PathVariable("id") Long id);

    @org.springframework.web.bind.annotation.PutMapping("/api/products/{id}/reduce-quantity")
    void reduceQuantity(@PathVariable("id") Long id, @org.springframework.web.bind.annotation.RequestParam("quantity") Integer quantity);
}
