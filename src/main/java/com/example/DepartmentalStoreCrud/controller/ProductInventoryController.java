package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.ProductInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productDetails")
public class ProductInventoryController {

    @Autowired
    private ProductInventoryService productInventoryService;

    @Operation(operationId = "getAllProducts", summary = "Get all Products")
    @GetMapping
    public List<ProductInventory> getAllProducts() {
        return productInventoryService.getAllProducts();
    }

    @Operation(operationId = "addProductDetails", summary = "Add Product Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> addProductDetails(@RequestBody ProductInventory productInventory) {
        productInventoryService.addProductDetails(productInventory);
        return ResponseEntity.ok("Product added successfully.");
    }

    @Operation(operationId = "updateProductDetails", summary = "Update Product Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{productID}")
    public ResponseEntity<String> updateProductDetails(@RequestBody ProductInventory productInventory) {
        productInventoryService.updateProductDetails(productInventory);
        return ResponseEntity.ok("Product updated successfully.");
    }

    @Operation(operationId = "deleteProduct", summary = "Delete Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{productID}")
    public ResponseEntity<String> deleteProductDetails(@PathVariable Long productID) {
        productInventoryService.deleteProductDetails(productID);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}