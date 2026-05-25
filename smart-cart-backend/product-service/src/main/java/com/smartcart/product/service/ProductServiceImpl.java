package com.smartcart.product.service;

import com.smartcart.product.dto.ProductRequestDTO;
import com.smartcart.product.dto.ProductResponseDTO;
import com.smartcart.product.entity.Product;
import com.smartcart.product.exception.ResourceNotFoundException;
import com.smartcart.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        log.info("Creating a new product with name: {}", requestDTO.getName());
        
        Product product = Product.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .quantity(requestDTO.getQuantity())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        
        return mapToResponseDTO(savedProduct);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        log.info("Updating product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setQuantity(requestDTO.getQuantity());

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        
        return mapToResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        log.info("Fetching all products");
        
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
                
        return mapToResponseDTO(product);
    }

    @Override
    public void reduceQuantity(Long id, Integer quantity) {
        log.info("Reducing quantity for product ID: {} by {}", id, quantity);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
                
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient product inventory");
        }
        
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Quantity reduced successfully. New quantity: {}", product.getQuantity());
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }
}
