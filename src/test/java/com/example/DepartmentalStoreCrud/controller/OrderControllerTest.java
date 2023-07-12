package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private OrderController orderController;

    @Test
    void testAddOrderDetails_OutOfStock() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        doThrow(IllegalStateException.class).when(orderService).addOrderDetails(order);
        // Act
        ResponseEntity<String> response = orderController.addOrderDetails(order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order placed successfully but out of stock. We will notify you once it is in stock", response.getBody());
        //verify(orderService, times(1)).addOrderDetails(order);
    }

    @Test
    void testGetOrderById_NonExistingOrder() {
        // Arrange
        Long orderId = 1L;
        Mockito.when(orderService.getOrderById(orderId)).thenThrow(NoSuchElementException.class);

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> orderController.getOrderById(orderId));
        verify(orderService, times(1)).getOrderById(orderId);
    }

    private Order createOrder(Long orderId) {
        Order order = new Order();
        order.setOrderID(orderId);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setOrderQuantity(2);
        order.setDiscount(10.0);
        order.setDiscountedPrice(90.0);

        Customer customer = new Customer();
        customer.setCustomerID(1L);
        customer.setFullName("John Doe");
        order.setCustomer(customer);

        ProductInventory productInventory = new ProductInventory();
        productInventory.setProductID(1L);
        productInventory.setProductName("Product 1");
        productInventory.setProductQuantity(5);
        productInventory.setPrice(100.0);
        order.setProductInventory(productInventory);
        return order;
    }
}
