package com.storesystem.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.ProductSupplier;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long>, JpaSpecificationExecutor<ProductSupplier>{

	@Override
    @EntityGraph(attributePaths = {"product", "supplier"}) // 一次性抓取，避免 N+1
	Page<ProductSupplier> findAll(Specification<ProductSupplier> spec, Pageable pageable);
	
}
