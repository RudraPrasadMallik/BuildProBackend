package com.buildpro.productservice.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.buildpro.productservice.dto.ProductDto;
import com.buildpro.productservice.dto.SectionDto;
import com.buildpro.productservice.dto.SectionWithProductsDto;
import com.buildpro.productservice.entity.Section;
import com.buildpro.productservice.repository.SectionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public List<SectionDto> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(section -> {
                    SectionDto dto = new SectionDto();
                    dto.setId(section.getId());
                    dto.setName(section.getName());
                    dto.setPriority(section.getPriority());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public SectionDto addSection(SectionDto dto) {
        Section section = new Section();
        section.setName(dto.getName());
        section.setPriority(dto.getPriority());
        section = sectionRepository.save(section);
        dto.setId(section.getId());
        return dto;
    }

    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    public SectionDto updateSection(Long id, SectionDto dto) {
        Section section = sectionRepository.findById(id).orElseThrow();
        section.setName(dto.getName());
        section = sectionRepository.save(section);
        dto.setId(section.getId());
        return dto;
    }
    
    
    public List<SectionWithProductsDto> getSectionsWithLimitedProducts() {
        // ✅ Sort by priority before mapping
        List<Section> sections = sectionRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Section::getPriority))
                .collect(Collectors.toList());

        return sections.stream().map(section -> {
            List<ProductDto> productDtos = section.getProducts().stream()
                    .limit(4)
                    .map(product -> {
                        ProductDto dto = new ProductDto();
                        dto.setId(product.getId());
                        dto.setName(product.getName());
                        dto.setPrice(product.getPrice());
                        dto.setImageUrl(product.getImageUrl());
                        dto.setDescription(product.getDescription());
                        dto.setSectionId(section.getId());
                        return dto;
                    }).collect(Collectors.toList());

            SectionWithProductsDto sectionDto = new SectionWithProductsDto();
            sectionDto.setId(section.getId());
            sectionDto.setName(section.getName());
            sectionDto.setProducts(productDtos);
            return sectionDto;
        }).collect(Collectors.toList());
    }
}
