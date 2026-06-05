package org.example.repository;

import org.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final Map<String, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(String.valueOf(idCounter.getAndIncrement()));
        }
        store.put(product.getId(), product);
        return product;
    }

    public boolean deleteById(String id) {
        return store.remove(id) != null;
    }

    public boolean existsById(String id) {
        return store.containsKey(id);
    }
}
