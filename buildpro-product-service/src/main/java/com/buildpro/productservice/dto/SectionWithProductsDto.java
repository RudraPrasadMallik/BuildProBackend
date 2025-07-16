package com.buildpro.productservice.dto;

import java.util.List;

import lombok.Data;
@Data
public class SectionWithProductsDto {
    private Long id;
    private String name;
    private List<ProductDto> products;
}
