package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getAllCustomersTest() throws Exception {
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomer(1L)); // Sample customer with ID 1L
        customers.add(createCustomer(2L));
        when(customerService.getAllCustomers()).thenReturn(customers);
        this.mockMvc.perform(get("/customerDetails"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(customers.size())));
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerByIDTest_CustomerFound() throws Exception {
        Customer customer = createCustomer(1L);
        when(customerService.getCustomerById(anyLong())).thenReturn(customer);
        this.mockMvc.perform(get("/customerDetails/{customerID}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fullName", is(customer.getFullName())))
                .andExpect(jsonPath("$.address", is(customer.getAddress())))
                .andExpect(jsonPath("$.contactNumber", is(customer.getContactNumber())))
                .andExpect(jsonPath("$.emailID", is(customer.getEmailID())));
        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void getOrdersByCustomerTest() throws Exception {
        Long customerId = 1L;
        Customer customer = createCustomer(customerId);
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(1L));
        orders.add(createOrder(2L));
        when(customerService.getOrdersByCustomer(anyLong())).thenReturn(orders);

        mockMvc.perform(get("/customerDetails/{customerID}/orders", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].orderID").exists())
                .andExpect(jsonPath("$[0].orderTimestamp").exists())
                .andExpect(jsonPath("$[0].orderQuantity").exists())
                .andExpect(jsonPath("$[0].discount").exists())
                .andExpect(jsonPath("$[0].discountedPrice").exists())
                .andExpect(jsonPath("$[0].customer.customerID").exists())
                .andExpect(jsonPath("$[0].customer.fullName").exists())
                .andExpect(jsonPath("$[0].productInventory.productID").exists())
                .andExpect(jsonPath("$[0].productInventory.productName").exists())
                .andExpect(jsonPath("$[0].productInventory.productQuantity").exists())
                .andExpect(jsonPath("$[0].productInventory.price").exists())
                .andExpect(jsonPath("$[1].orderID").exists())
                .andExpect(jsonPath("$[1].orderTimestamp").exists())
                .andExpect(jsonPath("$[1].orderQuantity").exists())
                .andExpect(jsonPath("$[1].discount").exists())
                .andExpect(jsonPath("$[1].discountedPrice").exists())
                .andExpect(jsonPath("$[1].customer.customerID").exists())
                .andExpect(jsonPath("$[1].customer.fullName").exists())
                .andExpect(jsonPath("$[1].productInventory.productID").exists())
                .andExpect(jsonPath("$[1].productInventory.productName").exists())
                .andExpect(jsonPath("$[1].productInventory.productQuantity").exists())
                .andExpect(jsonPath("$[1].productInventory.price").exists());

        verify(customerService, times(1)).getOrdersByCustomer(customerId);
    }

    @Test
    void addCustomer() throws Exception {
        Customer customer = createCustomer(1L);
        when(customerService.addCustomerDetails(any(Customer.class))).thenReturn(customer);
        this.mockMvc.perform(post("/customerDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Customer added successfully"));
        verify(customerService, times(1)).addCustomerDetails(customer);
    }

    @Test
    void updateCustomer() throws Exception {
        Customer customer = createCustomer(1L);
        when(customerService.updateCustomerDetails(anyLong(), any(Customer.class))).thenReturn(customer);
        this.mockMvc.perform(put("/customerDetails/{customerID}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer updated successfully with id: " + customer.getCustomerID()));
        verify(customerService, times(1)).updateCustomerDetails(customer.getCustomerID(), customer);
    }

    @Test
    void deleteCustomerTest() throws Exception {
        doNothing().when(customerService).deleteCustomerDetails(anyLong());
        this.mockMvc.perform(delete("/customerDetails/{customerID}", 2L))
                .andExpect(status().isOk());
         verify(customerService, times(1)).deleteCustomerDetails(2L);
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
