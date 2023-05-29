package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orderDetails")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orderDetails/{orderID}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderID)
    {
        Order order = orderService.getOrderById(orderID);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orderDetails")
    public ResponseEntity<String> addOrderDetails(@RequestBody Order order) {
        try {
            orderService.addOrderDetails(order);
            return ResponseEntity.ok("Order placed successfully.");
        }
        catch(IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/orderDetails/{orderID}")
    public ResponseEntity<String> updateOrderDetails(@RequestBody Order order) {
        try {
            orderService.updateOrderDetails(order);
            return ResponseEntity.ok("Order updated successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/orderDetails/{orderID}")
    public ResponseEntity<String> deleteOrderDetails(@PathVariable Long orderID, Order order) {
        orderService.deleteOrderDetails(orderID, order);
        return ResponseEntity.ok("Order deleted successfully.");
    }
}