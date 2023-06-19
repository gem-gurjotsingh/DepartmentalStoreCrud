package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Retrieves all orders.
     *
     * @return List of orders.
     */
    @Operation(operationId = "getAllOrders", summary = "Get all Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    /**
     * Retrieves an order by ID.
     *
     * @param orderID The ID of the order to retrieve.
     * @return The order with the specified ID.
     */
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

    /**
     * Adds a new order.
     *
     * @param order The order to add.
     * @return A response entity indicating the status of the operation.
     */
    @Operation(operationId = "addOrderDetails", summary = "Add Order Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> addOrderDetails(@RequestBody Order order) {
        orderService.addOrderDetails(order);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order placed successfully.");
    }

    /**
     * Updates an existing order.
     *
     * @param orderID The ID of the order to update.
     * @param order   The updated order.
     * @return A response entity indicating the status of the operation.
     */
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

    /**
     * Deletes an order by ID.
     *
     * @param orderID The ID of the order to delete.
     * @param order   The order to delete.
     * @return A response entity indicating the status of the operation.
     */
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