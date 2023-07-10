package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Environment environment;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(environment.getProperty("email.regexp")).thenReturn("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        when(environment.getProperty("contact.regexp")).thenReturn("^\\+91[6789]\\d{9}$");

        // Set the mocked environment in the customerService instance
        customerService.setEmailRegexp(environment.getProperty("email.regexp"));
        customerService.setContactRegexp(environment.getProperty("contact.regexp"));
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomer(1L)); // Sample customer with ID 1L
        customers.add(createCustomer(2L));
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<Customer> result = customerService.getAllCustomers();

        // Assert
        assertEquals(customers.size(), result.size());
        assertEquals(customers, result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testGetCustomerById_ExistingCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer customer = createCustomer(customerId); // Sample customer with ID 1L
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Argument captor
        ArgumentCaptor<Long> customerIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Act
        Customer result = customerService.getCustomerById(customerId);

        // Assert
        assertEquals(customer, result);
        verify(customerRepository, times(1)).findById(customerIdCaptor.capture());
        assertEquals(customerId, customerIdCaptor.getValue());
    }


    @Test
    void testGetCustomerById_NonExistingCustomer() {
        // Arrange
        Long customerId = 3L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act
        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void testGetOrdersByCustomer() {
        // Arrange
        Long customerId = 1L;
        Customer customer = createCustomer(customerId);
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(1L)); // Sample order with ID 1L and matching customer ID
        orders.add(createOrder(2L));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(orderRepository.findByCustomer_CustomerID(customerId)).thenReturn(orders);

        // Argument captor
        ArgumentCaptor<Long> customerIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Act
        List<Order> result = customerService.getOrdersByCustomer(customerId);

        // Assert
        assertEquals(orders.size(), result.size());
        assertEquals(orders, result);
        verify(customerRepository, times(1)).findById(customerIdCaptor.capture());
        assertEquals(customerId, customerIdCaptor.getValue());
        verify(orderRepository, times(1)).findByCustomer_CustomerID(customerIdCaptor.capture());
        assertEquals(customerId, customerIdCaptor.getValue());
    }

    @Test
    void testAddCustomerDetails_ValidEmailContact() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L

        when(customerRepository.save(customer)).thenReturn(customer);

        // Argument captor
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        // Act
        customerService.addCustomerDetails(customer);

        // Assert
        verify(customerRepository, times(1)).save(customerCaptor.capture());
        assertEquals(customer, customerCaptor.getValue());
    }

    @Test
    void testAddCustomerDetails_InvalidEmail() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        customer.setEmailID("invalid-email");
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomerDetails(customer));
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testAddCustomerDetails_InvalidContact() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        customer.setContactNumber("1234567");
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.addCustomerDetails(customer));
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testUpdateCustomerDetails_ValidEmailContact() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L

        when(customerRepository.findById(customer.getCustomerID())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Argument captor
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);

        // Act
        customerService.updateCustomerDetails(customer.getCustomerID(), customer);

        // Assert
        verify(customerRepository, times(1)).save(customerCaptor.capture());
        assertEquals(customer, customerCaptor.getValue());
    }

    @Test
    void testUpdateCustomerDetails_InvalidEmail() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        customer.setEmailID("invalid-email");

        when(customerRepository.findById(customer.getCustomerID())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomerDetails(customer.getCustomerID(), customer));

        // Assert
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testUpdateCustomerDetails_InvalidContact() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        customer.setContactNumber("1234567");

        when(customerRepository.findById(customer.getCustomerID())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomerDetails(customer.getCustomerID(), customer));

        // Assert
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testDeleteCustomerDetails_ExistingCustomer() {
        // Arrange
        Long customerId = 3L;
        Customer customer = createCustomer(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        ArgumentCaptor<Long> customerIdCaptor = ArgumentCaptor.forClass(Long.class);

        // Act
        customerService.deleteCustomerDetails(customerId);

        // Assert
        verify(customerRepository, times(1)).deleteById(customerIdCaptor.capture());
        assertEquals(customerId, customerIdCaptor.getValue());
    }

    @Test
    void testDeleteCustomerDetails_NonExistingCustomer() {
        // Arrange
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> customerService.deleteCustomerDetails(customerId));
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).delete(any());
    }

    private Customer createCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerID(customerId);
        customer.setFullName("John Doe");
        customer.setAddress("123 Main Street");
        customer.setContactNumber("+919417665710");
        customer.setEmailID("johndoe@gmail.com");
        return customer;
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