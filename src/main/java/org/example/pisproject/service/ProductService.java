package org.example.pisproject.service;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.entity.Product;
import org.example.pisproject.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    // We generally don’t want to shard products, since they’re global/catalog-level data.

    private final ProductRepository productRepository;

    @Transactional
    @CacheEvict(value = "allProducts", allEntries = true)
    @CachePut(value = "products", key = "#result.id")
    public Product createProduct(Product product) {
        product.setOrderDate(LocalDate.now());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "allProducts", allEntries = true)
    public Product updateProduct(Long id, Product updated) {
        updated.setId(id);
        return productRepository.save(updated);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "allProducts", allEntries = true)
    })
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Cacheable(value = "allProducts")
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}