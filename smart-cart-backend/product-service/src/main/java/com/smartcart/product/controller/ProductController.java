package com.smartcart.product.controller;

import com.smartcart.product.dto.ProductRequestDTO;
import com.smartcart.product.dto.ProductResponseDTO;
import com.smartcart.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        log.info("REST request to create product: {}", productRequestDTO.getName());
        return new ResponseEntity<>(productService.createProduct(productRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        log.info("REST request to update product ID: {}", id);
        return ResponseEntity.ok(productService.updateProduct(id, productRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        log.info("REST request to get all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        log.info("REST request to get product ID: {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}/reduce-quantity")
    public ResponseEntity<Void> reduceQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("REST request to reduce quantity for product ID: {} by {}", id, quantity);
        productService.reduceQuantity(id, quantity);
        return ResponseEntity.ok().build();
    }
}
