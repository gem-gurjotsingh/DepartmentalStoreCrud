package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomer(1L)); // Sample customer with ID 1L
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<Customer> result = customerService.getAllCustomers();

        // Assert
        assertEquals(1, result.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testAddCustomerDetails_ValidEmail() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        customerService.addCustomerDetails(customer);

        // Assert
        verify(customerRepository, times(1)).save(customer);
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
    void testUpdateCustomerDetails_ValidEmail() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        when(customerRepository.findById(customer.getCustomerID())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        customerService.updateCustomerDetails(customer.getCustomerID(),customer);

        // Assert
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomerDetails_InvalidEmail() {
        // Arrange
        Customer customer = createCustomer(1L); // Sample customer with ID 1L
        customer.setEmailID("invalid-email");
        when(customerRepository.findById(customer.getCustomerID())).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomerDetails(customer.getCustomerID(), customer));
        verify(customerRepository, never()).save(customer);
    }

    @Test
    void testDeleteCustomerDetails() {
        // Arrange
        Long customerId = 1L;
        Customer customer = createCustomer(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomerDetails(customerId);

        // Assert
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    private Customer createCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerID(customerId);
        customer.setFullName("John Doe");
        customer.setAddress("123 Main Street");
        customer.setContactNumber("123-456-7890");
        customer.setEmailID("johndoe@example.com");
        return customer;
    }
}
