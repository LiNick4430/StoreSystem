package com.storesystem.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{

}
