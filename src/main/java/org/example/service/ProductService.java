package org.example.service;

import org.example.exception.ProductNotFoundException;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        product.setId(null);
        return repository.save(product);
    }

    public Product updateProduct(String id, Product updated) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        updated.setId(id);
        return repository.save(updated);
    }

    public void deleteProduct(String id) {
        if (!repository.deleteById(id)) {
            throw new ProductNotFoundException(id);
        }
    }
}
