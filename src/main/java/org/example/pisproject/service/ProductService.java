package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.Product;
import org.example.pisproject.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    // We generally don’t want to shard products, since they’re global/catalog-level data.

    private final ProductRepository productRepository;

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product updateProduct(Long id, Product updated) {
        updated.setId(id);
        return productRepository.save(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}