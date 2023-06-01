package com.example.DepartmentalStoreCrud.service;// OrderServiceTest.java

import com.example.DepartmentalStoreCrud.bean.*;
import com.example.DepartmentalStoreCrud.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BackorderService backorderService;

    @Mock
    private ProductInventoryRepository productInventoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BackorderRepository backorderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(1L)); // Sample order with ID 1L
        when(orderRepository.findAll()).thenReturn(orders);

        // Act
        List<Order> result = orderService.getAllOrders();

        // Assert
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById_ExistingOrder() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrderById(1L);

        // Assert
        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_NonExistingOrder() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }

    void testAddOrderDetails_ProductAvailable() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        ProductInventory productInventory = order.getProductInventory();
        when(orderRepository.save(order)).thenReturn(order);
        when(productInventoryRepository.findById(productInventory.getProductID())).thenReturn(Optional.of(productInventory));

        // Act
        orderService.addOrderDetails(order);

        // Assert
        verify(orderRepository, times(1)).save(order);
        verify(productInventoryRepository, times(1)).save(productInventory);
        verify(productInventoryRepository, times(1)).findById(productInventory.getProductID());
    }

    @Test
    void testDeleteOrderDetails_OrderFound() {
        // Arrange
        Long orderId = 1L;
        Order order = createOrder(orderId); // Sample order with ID 1L
        Backorder backorder = new Backorder();
        backorder.setBackorderID(1L);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(backorderRepository.findByOrder(order)).thenReturn(backorder);

        // Act
        orderService.deleteOrderDetails(orderId, order);

        // Assert
        verify(backorderService, times(1)).deleteBackorder(backorder.getBackorderID());
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void testDeleteOrderDetails_OrderNotFound() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> orderService.deleteOrderDetails(orderId, null));
        verify(backorderService, never()).deleteBackorder(anyLong());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, never()).deleteById(orderId);
    }

    // Add more unit tests for other methods in OrderService

    // Helper method to create a sample Order object
    private Order createOrder(Long orderId) {
        Order order = new Order();
        order.setOrderID(orderId);

        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductID(1L);
        productInventory.setProductName("Sample Product");
        productInventory.setPrice(10.0);
        productInventory.setCount(10);
        productInventory.setAvailability(true);

        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setFullName("John Doe");

        order.setProductInventory(productInventory);
        order.setCustomer(customer);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setQuantity(5);
        order.setDiscount(0.0);
        order.setDiscountedPrice(10.0);

        return order;
    }
}
