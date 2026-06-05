package org.example.service;

import org.example.exception.ProductNotFoundException;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(new ProductRepository());
    }

    @Test
    void createProduct_assignsId() {
        Product p = service.createProduct(new Product(null, "Widget", "A widget", 9.99, 100));
        assertNotNull(p.getId());
        assertEquals("Widget", p.getName());
    }

    @Test
    void getAllProducts_returnsAll() {
        service.createProduct(new Product(null, "A", "desc", 1.0, 1));
        service.createProduct(new Product(null, "B", "desc", 2.0, 2));
        List<Product> all = service.getAllProducts();
        assertEquals(2, all.size());
    }

    @Test
    void getProductById_notFound_throws() {
        assertThrows(ProductNotFoundException.class, () -> service.getProductById("99"));
    }

    @Test
    void updateProduct_updatesFields() {
        Product created = service.createProduct(new Product(null, "Old", "desc", 5.0, 10));
        Product updated = service.updateProduct(created.getId(), new Product(null, "New", "desc", 15.0, 20));
        assertEquals("New", updated.getName());
        assertEquals(15.0, updated.getPrice());
    }

    @Test
    void deleteProduct_removesIt() {
        Product created = service.createProduct(new Product(null, "ToDelete", "desc", 1.0, 1));
        service.deleteProduct(created.getId());
        assertThrows(ProductNotFoundException.class, () -> service.getProductById(created.getId()));
    }

    @Test
    void deleteProduct_notFound_throws() {
        assertThrows(ProductNotFoundException.class, () -> service.deleteProduct("999"));
    }
}
