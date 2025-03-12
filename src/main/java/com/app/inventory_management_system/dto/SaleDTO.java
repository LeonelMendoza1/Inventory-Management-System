package com.app.inventory_management_system.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDTO {
    private Long id;
    private LocalDateTime saleDate;
    private double total;
    private List<SaleItemDTO> saleItems;
}
