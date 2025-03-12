package com.app.inventory_management_system.service;

import com.app.inventory_management_system.dto.CategoryDTO;
import com.app.inventory_management_system.dto.ProductDTO;
import com.app.inventory_management_system.dto.SaleDTO;
import com.app.inventory_management_system.dto.SaleItemDTO;
import com.app.inventory_management_system.model.Category;
import com.app.inventory_management_system.model.Product;
import com.app.inventory_management_system.model.Sale;
import com.app.inventory_management_system.model.SaleItem;
import com.app.inventory_management_system.repository.ProductRepository;
import com.app.inventory_management_system.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    @Transactional
    public SaleDTO createSale(Sale sale) {
        sale.setSaleDate(LocalDateTime.now());
        double total = 0.0;

        for (SaleItem item : sale.getSaleItems()) {
            Optional<Product> productOptional = productRepository.findById(item.getProduct().getId());
            if (productOptional.isEmpty()) {
                throw new RuntimeException("Product not found");
            }
            Product existingProduct = productOptional.get();
            if (existingProduct.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + existingProduct.getName());
            }
            item.setProduct(existingProduct); // Establecer el producto completo
            item.setSale(sale);
            item.setSubtotal(item.getQuantity() * existingProduct.getPrice());
            total += item.getSubtotal();
            existingProduct.setStock(existingProduct.getStock() - item.getQuantity());
            productRepository.save(existingProduct);
        }

        sale.setTotal(total);
        Sale savedSale = saleRepository.save(sale);
        return convertToDTO(savedSale);
    }

    private SaleDTO convertToDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(sale.getId());
        saleDTO.setSaleDate(sale.getSaleDate());
        saleDTO.setTotal(sale.getTotal());
        saleDTO.setSaleItems(sale.getSaleItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        return saleDTO;
    }

    private SaleItemDTO convertToDTO(SaleItem saleItem) {
        SaleItemDTO saleItemDTO = new SaleItemDTO();
        saleItemDTO.setId(saleItem.getId());
        saleItemDTO.setProduct(convertToDTO(saleItem.getProduct()));
        saleItemDTO.setQuantity(saleItem.getQuantity());
        saleItemDTO.setSubtotal(saleItem.getSubtotal());
        return saleItemDTO;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setCategory(convertToDTO(product.getCategory()));
        return productDTO;
    }

    private CategoryDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
