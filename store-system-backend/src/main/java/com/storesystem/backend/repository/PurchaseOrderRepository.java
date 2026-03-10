package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.PurchaseOrder;



@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder>{

	@Override
    @EntityGraph(attributePaths = {"supplier"})
    Page<PurchaseOrder> findAll(Specification<PurchaseOrder> spec, Pageable pageable);
	
	Optional<PurchaseOrder> findByPurchaseOrderId(Long orderId);
	
	Optional<PurchaseOrder> findByPurchaseOrderNumber(String purchaseOrderNumber);
}
