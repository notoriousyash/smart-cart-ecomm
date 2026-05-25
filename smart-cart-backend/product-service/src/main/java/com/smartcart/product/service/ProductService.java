package com.smartcart.product.service;

import com.smartcart.product.dto.ProductRequestDTO;
import com.smartcart.product.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long id);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
    void reduceQuantity(Long id, Integer quantity);
}
