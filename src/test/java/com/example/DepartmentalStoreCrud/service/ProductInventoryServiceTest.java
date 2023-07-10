package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        products.add(createProduct(2L));
        when(productInventoryRepository.findAll()).thenReturn(products);

        // Act
        List<ProductInventory> result = productInventoryService.getAllProducts();

        // Assert
        assertEquals(products.size(), result.size());
        assertEquals(products, result);
        verify(productInventoryRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_ExistingProduct() {
        // Arrange
        Long productId = 1L;
        ProductInventory productInventory = createProduct(productId); // Sample order with ID 1L
        when(productInventoryRepository.findById(productId)).thenReturn(Optional.of(productInventory));

        //Argument Captor
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Act
        ProductInventory result = productInventoryService.getProductById(productId);

        // Assert
        assertEquals(productInventory, result);
        verify(productInventoryRepository, times(1)).findById(productIdCaptor.capture());
        assertEquals(productId, productIdCaptor.getValue());
    }

    @Test
    void testGetProductById_NonExistingProduct() {
        // Arrange
        Long productId = 1L;
        when(productInventoryRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        assertThrows(NoSuchElementException.class, () -> productInventoryService.getProductById(productId));
        verify(productInventoryRepository, times(1)).findById(productId);
    }

    @Test
    void testUpdateProductDetails() {
        // Arrange
        ProductInventory product = createProduct(1L); // Sample product with ID 1L
        when(productInventoryRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
        when(productInventoryRepository.save(product)).thenReturn(product);

        //Argument captor
        ArgumentCaptor<ProductInventory> productCaptor = ArgumentCaptor.forClass(ProductInventory.class);

        // Act
        productInventoryService.updateProductDetails(product.getProductID(), product);

        // Assert
        verify(productInventoryRepository, times(1)).save(productCaptor.capture());
        assertEquals(product, productCaptor.getValue());
    }

    @Test
    void testDeleteProductDetails_ExistingProduct() {
        // Arrange
        Long productId = 1L;
        ProductInventory productInventory = createProduct(productId);
        when(productInventoryRepository.findById(productId)).thenReturn(Optional.of(productInventory));

        //Argument Captor
        ArgumentCaptor<Long> productIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Act
        productInventoryService.deleteProductDetails(productId);

        // Assert
        verify(productInventoryRepository, times(1)).deleteById(productIdCaptor.capture());
        assertEquals(productId, productIdCaptor.getValue());
    }

    @Test
    void testDeleteProductDetails_NonExistingProduct() {
        // Arrange
        Long productId = 3L;
        when(productInventoryRepository.findById(productId)).thenReturn(Optional.empty());

        // Act
        assertThrows(NoSuchElementException.class, () -> productInventoryService.deleteProductDetails(productId));

        // Assert
        verify(productInventoryRepository, times(1)).findById(productId);
        verify(productInventoryRepository, never()).delete(any());
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
