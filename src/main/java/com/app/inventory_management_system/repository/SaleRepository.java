package com.app.inventory_management_system.repository;


import com.app.inventory_management_system.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
