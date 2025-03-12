package com.app.inventory_management_system.service;

import com.app.inventory_management_system.model.Category;
import com.app.inventory_management_system.model.Product;
import com.app.inventory_management_system.repository.CategoryRepository;
import com.app.inventory_management_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        product.setCategory(category.get());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setStock(product.getStock());
            Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
            if (category.isEmpty()) {
                throw new RuntimeException("Category not found");
            }
            updatedProduct.setCategory(category.get());
            return productRepository.save(updatedProduct);
        }
        throw new RuntimeException("Product not found");
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
