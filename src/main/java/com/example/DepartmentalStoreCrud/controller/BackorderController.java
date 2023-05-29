package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.service.BackorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class BackorderController {

    @Autowired
    private BackorderService backorderService;

    @PostMapping("/backorders/{backorderId}")
    public ResponseEntity<String> createBackorder(@PathVariable Long orderId, @RequestBody Backorder backorder) {
        backorderService.createBackorder(orderId, backorder);
        return ResponseEntity.ok("Backorder created successfully.");
    }

    @GetMapping("/backorders")
    public List<Backorder> getAllBackorders() {
        return backorderService.getAllBackorders();
    }

    @GetMapping("/backorders/{backorderId}")
    public ResponseEntity<Backorder> getBackorderById(@PathVariable Long backorderId) {
        Backorder backorder = backorderService.getBackorderById(backorderId);
        return ResponseEntity.ok(backorder);
    }

    @DeleteMapping("/backorders/{backorderId}")
    public ResponseEntity<String> deleteBackorder(@PathVariable Long backorderId) {
        backorderService.deleteBackorder(backorderId);
        return ResponseEntity.ok("Backorder deleted successfully.");
    }
}