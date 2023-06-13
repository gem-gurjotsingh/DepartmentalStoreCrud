package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.service.BackorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/backorders")
public class BackorderController {

    @Autowired
    private BackorderService backorderService;

    // Create Backorder
    @Operation(operationId = "createBackorder", summary = "Create a new Backorder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backorder created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> createBackorder(@RequestBody Backorder backorder) {
        // Implementation
        return ResponseEntity.ok("Backorder created successfully.");
    }

    // Get All Backorders
    @Operation(operationId = "getAllBackorders", summary = "Get all Backorders")
    @GetMapping
    public List<Backorder> getAllBackorders() {
        // Implementation
        return backorderService.getAllBackorders();
    }

    // Get Backorder by ID
    @Operation(operationId = "getBackorderByID", summary = "Get Backorder by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backorder found"),
            @ApiResponse(responseCode = "404", description = "Backorder not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{backorderId}")
    public ResponseEntity<Backorder> getBackorderById(@PathVariable Long backorderId) {
        // Implementation
        Backorder backorder = backorderService.getBackorderById(backorderId);
        return ResponseEntity.ok(backorder);
    }

    // Delete Backorder
    @Operation(operationId = "deleteBackorder", summary = "Delete Backorder by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Backorder deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Backorder not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{backorderId}")
    public ResponseEntity<String> deleteBackorder(@PathVariable Long backorderId) {
        // Implementation
        backorderService.deleteBackorder(backorderId);
        return ResponseEntity.ok("Backorder deleted successfully.");
    }
}