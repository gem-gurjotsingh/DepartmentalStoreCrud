package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCustomers() {
        // Create a list of customers for the mock response
        List<Customer> customerList = new ArrayList<>();
        customerList.add(createCustomer(1L));
        customerList.add(createCustomer(2L));
        Mockito.when(customerService.getAllCustomers()).thenReturn(customerList);
        // Call the controller method
        List<Customer> result = customerController.getAllCustomers();

        // Verify that the service method was called once
        verify(customerService, times(1)).getAllCustomers();

        // Assert the expected result
        assertEquals(customerList, result);
    }


    // Utility method to create a Customer object
    private Customer createCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setCustomerID(customerId);
        customer.setFullName("John Doe");
        customer.setAddress("123 Main Street");
        customer.setContactNumber("+919417665710");
        customer.setEmailID("johndoe@gmail.com");
        return customer;
    }
}
