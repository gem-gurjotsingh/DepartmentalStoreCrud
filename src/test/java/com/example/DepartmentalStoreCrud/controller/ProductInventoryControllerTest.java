package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.ProductInventoryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(ProductInventoryController.class)
public class ProductInventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductInventoryService productInventoryService;

    @InjectMocks
    private ProductInventoryController productInventoryController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(productInventoryController).build();
    }

    @Test
    void getAllProductsTest() throws Exception {
        List<ProductInventory> products = new ArrayList<>();
        products.add(createProduct(1L));
        products.add(createProduct(2L));

        when(productInventoryService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/productDetails"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(products.size())));

        verify(productInventoryService, times(1)).getAllProducts();
    }

    @Test
    void getProductByIdTest_ProductFound() throws Exception {
        Long productId = 1L;
        ProductInventory product = createProduct(productId);

        when(productInventoryService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/productDetails/{productID}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productID", is(product.getProductID().intValue())))
                .andExpect(jsonPath("$.productName", is(product.getProductName())))
                .andExpect(jsonPath("$.productQuantity", is(product.getProductQuantity())))
                .andExpect(jsonPath("$.price", is(product.getPrice())));

        verify(productInventoryService, times(1)).getProductById(productId);
    }

    @Test
    void addProductDetailsTest() throws Exception {
        ProductInventory product = createProduct(1L);

        mockMvc.perform(post("/productDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product added successfully."));

        verify(productInventoryService, times(1)).addProductDetails(product);
    }

    @Test
    void updateProductDetailsTest() throws Exception {
        Long productId = 1L;
        ProductInventory product = createProduct(productId);

        mockMvc.perform(put("/productDetails/{productID}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully."));

        verify(productInventoryService, times(1)).updateProductDetails(eq(productId), any(ProductInventory.class));
    }

    @Test
    void deleteProductDetailsTest() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/productDetails/{productID}", productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully."));

        verify(productInventoryService, times(1)).deleteProductDetails(productId);
    }

    private ProductInventory createProduct(Long productId) {
        ProductInventory product = new ProductInventory();
        product.setProductID(productId);
        product.setProductDesc("Product description");
        product.setProductName("Product name");
        product.setPrice(9.99); // Set the desired price
        product.setProductQuantity(10); // Set the desired count

        return product;
    }
}
