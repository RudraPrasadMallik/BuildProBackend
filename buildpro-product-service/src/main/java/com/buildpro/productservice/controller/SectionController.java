package com.buildpro.productservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildpro.productservice.dto.SectionDto;
import com.buildpro.productservice.dto.SectionWithProductsDto;
import com.buildpro.productservice.service.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;
    
    @GetMapping("/home")
    public ResponseEntity<List<SectionWithProductsDto>> getHomeData() {
        return ResponseEntity.ok(sectionService.getSectionsWithLimitedProducts());
    }

    

    // 👉 USER: Get all sections
    @GetMapping("/sections")
    public ResponseEntity<List<SectionDto>> getAllSections() {
        return ResponseEntity.ok(sectionService.getAllSections());
    }

    // 👉 ADMIN: Add section
    @PostMapping("/admin/sections")
    public ResponseEntity<SectionDto> addSection(@RequestBody SectionDto dto) {
        return ResponseEntity.ok(sectionService.addSection(dto));
    }

    // 👉 ADMIN: Update section
    @PutMapping("/admin/sections/{id}")
    public ResponseEntity<SectionDto> updateSection(@PathVariable Long id, @RequestBody SectionDto dto) {
        return ResponseEntity.ok(sectionService.updateSection(id, dto));
    }

    // 👉 ADMIN: Delete section
    @DeleteMapping("/admin/sections/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}
