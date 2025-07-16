package com.buildpro.productservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildpro.productservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findBySectionId(Long sectionId);
}
