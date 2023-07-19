package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.OrderService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void getAllOrdersTest() throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(1L));
        orders.add(createOrder(2L));

        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/orderDetails"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(orders.size())));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    public void getOrderByIdTest_OrderNotFound() throws Exception {
        Long nonExistingOrderId = 999L;
        when(orderService.getOrderById(nonExistingOrderId)).thenReturn(null);

        mockMvc.perform(get("/{orderID}", nonExistingOrderId))
                .andExpect(status().isNotFound());

        verify(orderService, never()).getOrderById(nonExistingOrderId);
    }

    @Test
    public void addOrderDetailsTest() throws Exception {
        Order order = createOrder(1L);

        mockMvc.perform(post("/orderDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Order placed successfully."));

        verify(orderService, times(1)).addOrderDetails(order);
    }

    @Test
    public void updateOrderDetailsTest() throws Exception {
        Long orderId = 1L;
        Order order = createOrder(orderId);

        mockMvc.perform(put("/orderDetails/{orderID}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().string("Order updated successfully."));

        verify(orderService, times(1)).updateOrderDetails(eq(orderId), any(Order.class));
    }

    @Test
    public void deleteOrderDetailsTest() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(delete("/orderDetails/{orderID}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully."));

        verify(orderService, times(1)).deleteOrderDetails(orderId);
    }

    private Order createOrder(Long orderId) {
        Order order = new Order();
        order.setOrderID(orderId);
        order.setOrderTimestamp(LocalDateTime.now());
        order.setOrderQuantity(2);
        order.setDiscount(10.0);

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
