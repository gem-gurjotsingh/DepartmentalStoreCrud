package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(1L)); // Sample order with ID 1L
        when(orderService.getAllOrders()).thenReturn(orders);

        // Act
        List<Order> result = orderController.getAllOrders();

        // Assert
        assertEquals(1, result.size());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById_ExistingOrder() {
        // Arrange
        Long orderId = 1L;
        Order order = createOrder(orderId); // Sample order with ID 1L
        when(orderService.getOrderById(orderId)).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.getOrderById(orderId);
        Order result = response.getBody();

        // Assert
        assertEquals(order, result);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(orderService, times(1)).getOrderById(orderId);
    }

//    @Test
//    void testGetOrderById_NonExistingOrder() {
//        // Arrange
//        Long orderId = 1L;
//        when(orderService.getOrderById(orderId)).thenThrow(NoSuchElementException.class);
//
//        // Act
//        ResponseEntity<Order> response = orderController.getOrderById(orderId);
//
//        // Assert
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        verify(orderService, times(1)).getOrderById(orderId);
//    }

    @Test
    void testAddOrderDetails_Successful() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        Mockito.doNothing().when(orderService).addOrderDetails(order);

        // Act
        ResponseEntity<String> response = orderController.addOrderDetails(order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order placed successfully.", response.getBody());
        verify(orderService, times(1)).addOrderDetails(order);
    }

//    @Test
//    void testAddOrderDetails_OutOfStock() {
//        // Arrange
//        Order order = createOrder(1L); // Sample order with ID 1L
//        Mockito.doNothing().when(orderService).addOrderDetails(order);
//
//        // Act
//        ResponseEntity<String> response = orderController.addOrderDetails(order);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Order placed successfully but out of stock. We will notify you once it is in stock", response.getBody());
//        verify(orderService, times(1)).addOrderDetails(order);
//    }

    @Test
    void testUpdateOrderDetails_Successful() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        Mockito.doNothing().when(orderService).updateOrderDetails(order);

        // Act
        ResponseEntity<String> response = orderController.updateOrderDetails(order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order updated successfully.", response.getBody());
        verify(orderService, times(1)).updateOrderDetails(order);
    }

//    @Test
//    void testUpdateOrderDetails_OutOfStock() {
//        // Arrange
//        Order order = createOrder(1L); // Sample order with ID 1L
//        Mockito.doNothing().when(orderService).updateOrderDetails(order);
//
//        // Act
//        ResponseEntity<String> response = orderController.updateOrderDetails(order);
//
//        // Assert
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Order placed successfully but out of stock. We will notify you once it is in stock", response.getBody());
//        verify(orderService, times(1)).updateOrderDetails(order);
//    }

    @Test
    void testDeleteOrderDetails() {
        // Arrange
        Long orderId = 1L;
        Order order = createOrder(orderId); // Sample order with ID 1L

        // Act
        ResponseEntity<String> response = orderController.deleteOrderDetails(orderId, order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order deleted successfully.", response.getBody());
        verify(orderService, times(1)).deleteOrderDetails(orderId, order);
    }

    // Helper method to create a sample Order object
    private Order createOrder(Long orderId) {
        Order order = new Order();
        order.setOrderID(orderId);

        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductID(1L);
        productInventory.setProductName("Sample Product");
        productInventory.setPrice(10.0);
        productInventory.setCount(100);
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
