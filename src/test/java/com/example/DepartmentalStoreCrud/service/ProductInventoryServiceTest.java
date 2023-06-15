package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductInventoryServiceTest {

    @Mock
    private ProductInventoryRepository productInventoryRepository;

    @InjectMocks
    private ProductInventoryService productInventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        List<ProductInventory> products = new ArrayList<>();
        products.add(createProduct(1L)); // Sample product with ID 1L
        when(productInventoryRepository.findAll()).thenReturn(products);

        // Act
        List<ProductInventory> result = productInventoryService.getAllProducts();

        // Assert
        assertEquals(1, result.size());
        verify(productInventoryRepository, times(1)).findAll();
    }

    @Test
    void testAddProductDetails() {
        // Arrange
        ProductInventory product = createProduct(1L); // Sample product with ID 1L
        when(productInventoryRepository.save(product)).thenReturn(product);

        // Act
        productInventoryService.addProductDetails(product);

        // Assert
        verify(productInventoryRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProductDetails() {
        // Arrange
        ProductInventory product = createProduct(1L); // Sample product with ID 1L
        when(productInventoryRepository.save(product)).thenReturn(product);

        // Act
        productInventoryService.updateProductDetails(product);

        // Assert
        verify(productInventoryRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProductDetails() {
        // Arrange
        Long productId = 1L;
        doNothing().when(productInventoryRepository).deleteById(productId);

        // Act
        productInventoryService.deleteProductDetails(productId);

        // Assert
        verify(productInventoryRepository, times(1)).deleteById(productId);
    }

    private ProductInventory createProduct(Long productId) {
        ProductInventory product = new ProductInventory();
        product.setProductID(productId);
        product.setProductDesc("Product description");
        product.setProductName("Product name");
        product.setPrice(9.99); // Set the desired price
        product.setExpiry(LocalDate.now().plusMonths(6)); // Set the desired expiry date
        product.setCount(10); // Set the desired count
        product.setAvailability(true); // Set the desired availability

        return product;
    }

}
