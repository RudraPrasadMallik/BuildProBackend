package com.buildpro.productservice.service;

import com.buildpro.productservice.dto.ProductDto;
import com.buildpro.productservice.entity.Product;
import com.buildpro.productservice.entity.Section;
import com.buildpro.productservice.repository.ProductRepository;
import com.buildpro.productservice.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;

    public List<ProductDto> getProductsBySection(Long sectionId) {
        return productRepository.findBySectionId(sectionId).stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setImageUrl(product.getImageUrl());
                    dto.setSectionId(product.getSection().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ProductDto addProduct(ProductDto dto) {
        Section section = sectionRepository.findById(dto.getSectionId()).orElseThrow();

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setSection(section);

        product = productRepository.save(product);
        dto.setId(product.getId());
        return dto;
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public ProductDto updateProduct(Long productId, ProductDto dto) {
        Product product = productRepository.findById(productId).orElseThrow();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());

        if (!product.getSection().getId().equals(dto.getSectionId())) {
            Section newSection = sectionRepository.findById(dto.getSectionId()).orElseThrow();
            product.setSection(newSection);
        }

        product = productRepository.save(product);
        dto.setId(product.getId());
        return dto;
    }
}
