package org.example.pisproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.pisproject.dto.ProductDTO;
import org.example.pisproject.mapper.ProductMapper;
import org.example.pisproject.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
        return ResponseEntity.ok(productMapper.toDTO(productService.createProduct(productMapper.toEntity(product))));
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return productMapper.toDTOList(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        return productService.getProduct(id)
                .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO product) {
        return ResponseEntity.ok(productMapper.toDTO(productService.updateProduct(id, productMapper.toEntity(product))));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}