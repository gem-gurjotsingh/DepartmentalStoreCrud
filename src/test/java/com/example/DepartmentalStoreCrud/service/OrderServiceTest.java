package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.BackorderRepository;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
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
        Long orderId = 1L;
        Order order = createOrder(orderId); // Sample order with ID 1L
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act
        Order result = orderService.getOrderById(orderId);

        // Assert
        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrderById_NonExistingOrder() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(orderId));
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testAddOrderDetails_Successful() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        when(orderRepository.save(order)).thenReturn(order);
        when(productInventoryRepository.findById(order.getProductInventory().getProductID())).thenReturn(Optional.of(order.getProductInventory()));
        when(customerRepository.findById(order.getCustomer().getCustomerID())).thenReturn(Optional.of(order.getCustomer()));

        // Act
        orderService.addOrderDetails(order);

        // Assert
        verify(orderRepository, times(2)).save(order);
        verify(productInventoryRepository, times(1)).findById(order.getProductInventory().getProductID());
        verify(customerRepository, times(1)).findById(order.getCustomer().getCustomerID());
    }

    @Test
    void testAddOrderDetails_OutOfStock() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        ProductInventory productInventory = order.getProductInventory();
        productInventory.setProductQuantity(0);
        when(orderRepository.save(order)).thenReturn(order);
        when(productInventoryRepository.findById(order.getProductInventory().getProductID())).thenReturn(Optional.of(productInventory));
        when(customerRepository.findById(order.getCustomer().getCustomerID())).thenReturn(Optional.of(order.getCustomer()));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> orderService.addOrderDetails(order));
        verify(orderRepository, times(2)).save(order);
        verify(productInventoryRepository, times(1)).findById(order.getProductInventory().getProductID());
        verify(customerRepository, times(1)).findById(order.getCustomer().getCustomerID());
        verify(backorderService, times(1)).createBackorder(any(Backorder.class));
    }

    @Test
    void testUpdateOrderDetails_Successful() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        when(orderRepository.save(order)).thenReturn(order);
        when(productInventoryRepository.findById(order.getProductInventory().getProductID())).thenReturn(Optional.of(order.getProductInventory()));
        when(customerRepository.findById(order.getCustomer().getCustomerID())).thenReturn(Optional.of(order.getCustomer()));

        // Act
        orderService.updateOrderDetails(order);

        // Assert
        verify(orderRepository, times(2)).save(order);
        verify(productInventoryRepository, times(1)).findById(order.getProductInventory().getProductID());
        verify(customerRepository, times(1)).findById(order.getCustomer().getCustomerID());
    }

    @Test
    void testUpdateOrderDetails_OutOfStock() {
        // Arrange
        Order order = createOrder(1L); // Sample order with ID 1L
        ProductInventory productInventory = order.getProductInventory();
        productInventory.setProductQuantity(0);
        when(orderRepository.save(order)).thenReturn(order);
        when(productInventoryRepository.findById(order.getProductInventory().getProductID())).thenReturn(Optional.of(order.getProductInventory()));
        when(customerRepository.findById(order.getCustomer().getCustomerID())).thenReturn(Optional.of(order.getCustomer()));

        // Act
        assertThrows(IllegalStateException.class, () -> orderService.updateOrderDetails(order));

        // Assert
        verify(orderRepository, times(2)).save(order);
        verify(productInventoryRepository, times(1)).findById(order.getProductInventory().getProductID());
        verify(customerRepository, times(1)).findById(order.getCustomer().getCustomerID());
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
