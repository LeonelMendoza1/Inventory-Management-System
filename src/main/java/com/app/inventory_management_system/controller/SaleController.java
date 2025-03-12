package com.app.inventory_management_system.controller;

import com.app.inventory_management_system.dto.SaleDTO;
import com.app.inventory_management_system.model.Sale;
import com.app.inventory_management_system.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales", description = "API for managing sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    @Operation(summary = "Get all sales", description = "Retrieve a list of all sales")
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID", description = "Retrieve a specific sale by its ID")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new sale", description = "Create a new sale and update product stock")
    public ResponseEntity<SaleDTO> createSale(@RequestBody Sale sale) {
        return ResponseEntity.ok(saleService.createSale(sale));
    }
}
