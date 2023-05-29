package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductInventoryController {

    @Autowired
    private ProductInventoryService productInventoryService;

    @GetMapping("/productDetails")
    public List<ProductInventory> getAllProducts() {
        return productInventoryService.getAllProducts();
    }

    @PostMapping("/productDetails")
    public ResponseEntity<String> addProductDetails(@RequestBody ProductInventory productInventory) {
        productInventoryService.addProductDetails(productInventory);
        return ResponseEntity.ok("Product added successfully.");
    }

    @PutMapping("/productDetails/{productID}")
    public ResponseEntity<String> updateProductDetails(@RequestBody ProductInventory productInventory) {
        productInventoryService.updateProductDetails(productInventory);
        return ResponseEntity.ok("Product updated successfully.");
    }

    @DeleteMapping("/productDetails/{productID}")
    public ResponseEntity<String> deleteProductDetails(@PathVariable Long productID) {
        productInventoryService.deleteProductDetails(productID);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}