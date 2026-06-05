package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String createProduct(String name, double price, int qty) throws Exception {
        Product p = new Product(null, name, "desc", price, qty);
        String body = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(body, Product.class).getId();
    }

    @Test
    void createAndGetProduct() throws Exception {
        String id = createProduct("Laptop", 999.99, 5);

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void getAllProducts_returnsList() throws Exception {
        createProduct("Mouse", 29.99, 50);
        createProduct("Keyboard", 79.99, 30);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void updateProduct() throws Exception {
        String id = createProduct("OldName", 10.0, 1);

        Product updated = new Product(null, "NewName", "desc", 20.0, 2);
        mockMvc.perform(put("/api/products/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    void deleteProduct() throws Exception {
        String id = createProduct("ToDelete", 5.0, 1);

        mockMvc.perform(delete("/api/products/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProduct_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/products/99999"))
                .andExpect(status().isNotFound());
    }
}
