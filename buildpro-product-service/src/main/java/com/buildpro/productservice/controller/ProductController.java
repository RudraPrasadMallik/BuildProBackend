package com.buildpro.productservice.controller;

import com.buildpro.productservice.dto.ProductDto;
import com.buildpro.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 👉 USER: Get products by section
    @GetMapping("/products/section/{sectionId}")
    public ResponseEntity<List<ProductDto>> getProductsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(productService.getProductsBySection(sectionId));
    }
  
    // 👉 ADMIN: Add product
    @PostMapping("/admin/products")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.addProduct(dto));
    }

    // 👉 ADMIN: Update product
    @PutMapping("/admin/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // 👉 ADMIN: Delete product
    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
