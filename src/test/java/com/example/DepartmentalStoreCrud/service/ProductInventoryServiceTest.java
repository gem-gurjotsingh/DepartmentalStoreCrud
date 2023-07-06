package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

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
        assertEquals(2, result.size());
        verify(productInventoryRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_ExistingProduct() {
        // Arrange
        Long productId = 1L;
        ProductInventory productInventory = createProduct(productId); // Sample order with ID 1L
        when(productInventoryRepository.findById(productId)).thenReturn(Optional.of(productInventory));

        // Act
        ProductInventory result = productInventoryService.getProductById(productId);

        // Assert
        assertEquals(productInventory, result);
        verify(productInventoryRepository, times(1)).findById(productId);
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

        // Act
        productInventoryService.updateProductDetails(product.getProductID(), product);

        // Assert
        verify(productInventoryRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProductDetails() {
        // Arrange
        Long productId = 1L;
        ProductInventory productInventory = createProduct(productId);
        when(productInventoryRepository.findById(productId)).thenReturn(Optional.of(productInventory));

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
        product.setProductQuantity(10); // Set the desired count

        return product;
    }

    private MultipartFile createMockMultipartFile(String fileName, String contentType, byte[] excelData) throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        doReturn(contentType).when(file).getContentType();
        when(file.getOriginalFilename()).thenReturn(fileName);
        when(file.getInputStream()).thenReturn(getClass().getResourceAsStream("/" + fileName));
        return file;
    }
}
