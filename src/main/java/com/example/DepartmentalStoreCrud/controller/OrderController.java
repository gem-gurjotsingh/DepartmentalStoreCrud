package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/orderDetails")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(operationId = "getAllOrders", summary = "Get all Orders")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(operationId = "getOrderByID", summary = "Get Order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{orderID}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderID)
    {
        return ResponseEntity.ok(orderService.getOrderById(orderID));
    }

    @Operation(operationId = "addOrderDetails", summary = "Add Order Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order placed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> addOrderDetails(@RequestBody Order order) {
        orderService.addOrderDetails(order);
        return ResponseEntity.ok("Order placed successfully.");
    }

    @Operation(operationId = "updateOrderDetails", summary = "Update Order Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{orderID}")
    public ResponseEntity<String> updateOrderDetails(@PathVariable Long orderID, @RequestBody Order order) {
            orderService.updateOrderDetails(order);
            return ResponseEntity.ok("Order updated successfully.");
    }

    @Operation(operationId = "deleteOrder", summary = "Delete Order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{orderID}")
    public ResponseEntity<String> deleteOrderDetails(@PathVariable Long orderID, Order order) {
        orderService.deleteOrderDetails(orderID, order);
        return ResponseEntity.ok("Order deleted successfully.");
    }
}