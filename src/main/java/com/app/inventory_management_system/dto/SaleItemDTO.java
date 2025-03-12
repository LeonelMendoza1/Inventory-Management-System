package com.app.inventory_management_system.dto;

import lombok.Data;

@Data
public class SaleItemDTO {
    private Long id;
    private ProductDTO product;
    private int quantity;
    private double subtotal;
}
