package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
        when(productInventoryRepository.findAll()).thenReturn(products);

        // Act
        List<ProductInventory> result = productInventoryService.getAllProducts();

        // Assert
        assertEquals(1, result.size());
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
    void testCheckExcelFormat_ValidFormat() throws Exception {
        // Arrange
        MultipartFile file = createMockMultipartFile("validFile.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new byte[]{});

        // Act
        boolean result = productInventoryService.checkExcelFormat(file);

        // Assert
        assertTrue(result);
    }

    @Test
    void testCheckExcelFormat_InvalidFormat() throws Exception {
        // Arrange
        MultipartFile file = createMockMultipartFile("invalidFile.txt", "text/plain", new byte[]{});

        // Act
        boolean result = productInventoryService.checkExcelFormat(file);

        // Assert
        assertFalse(result);
    }

    @Test
    void testSaveExcel_ValidFileFormat() throws Exception {
        // Arrange
        byte[] excelData = "productName,productDesc,price,productQuantity\nTestProduct,TestDescription,10.0,100".getBytes();
        MultipartFile file = createMockMultipartFile("validFile.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelData);
        InputStream inputStream = new ByteArrayInputStream(excelData); // Create input stream from byte array

        // Create mock of ProductInventoryService
        ProductInventoryService productInventoryServiceMock = mock(ProductInventoryService.class);

        // Mock behavior of productInventoryRepository
        when(productInventoryRepository.saveAll(any(List.class))).thenReturn(Collections.emptyList());

        // Mock behavior of productInventoryService's convertExcelToListOfProduct method
        when(productInventoryServiceMock.convertExcelToListOfProduct(any(InputStream.class))).thenReturn(Collections.emptyList());

        // Act
        productInventoryServiceMock.saveExcel(file);
    }


    @Test
    void testSaveExcel_InvalidFileFormat() throws Exception {
        // Arrange
        byte[] excelData = "invalid,data".getBytes();
        MultipartFile file = createMockMultipartFile("invalidFile.txt", "text/plain", excelData);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> productInventoryService.saveExcel(file));
        verify(productInventoryRepository, never()).saveAll(any(List.class));
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
    public void testDeleteBackordersCronJob() {
        // Arrange
        ProductInventory product1 = new ProductInventory();
        product1.setProductID(1L);
        product1.setProductName("Product 1");
        product1.setProductQuantity(10);

        ProductInventory product2 = new ProductInventory();
        product2.setProductID(2L);
        product2.setProductName("Product 2");
        product2.setProductQuantity(5);

        List<ProductInventory> products = Arrays.asList(product1, product2);

        // Mocking the productRepo.findAll() method
        when(productInventoryRepository.findAll()).thenReturn(products);

        // Act
        productInventoryService.deleteBackordersCronJob();

        // Assert
        verify(productInventoryRepository, times(1)).findAll();
        verify(productInventoryRepository, times(2)).save(any(ProductInventory.class));
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
