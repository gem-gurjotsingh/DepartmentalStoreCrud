package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.ProductInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping(path = "/productDetails")
public class ProductInventoryController {

    /**
     * Autowired ProductInventoryService
     */
    @Autowired
    private ProductInventoryService productInventoryService;

    /**
     * Retrieves all products.
     *
     * @return List of products.
     */
    @Operation(operationId = "getAllProducts", summary = "Get all Products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ProductInventory>> getAllProducts() {
        return new ResponseEntity<>(productInventoryService.getAllProducts(), HttpStatus.OK);
    }

    /**
     * Retrieves a product by ID.
     *
     * @param productID The ID of the product to retrieve.
     * @return The product with the specified ID.
     */
    @Operation(operationId = "getProductByID", summary = "Get Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(path = "/{productID}", produces = "application/json")
    public ResponseEntity<ProductInventory> getProductById(
            @Parameter(description = "The ID of the product to retrieve.", required = true)
            @PathVariable final Long productID) {
        return ResponseEntity.ok(productInventoryService.getProductById(productID));
    }

    /**
     * Adds a new product
     *
     * @param productInventory The product to add.
     * @return A response entity indicating the status of the operation.
     */
    @Operation(operationId = "addProductDetails", summary = "Add Product Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> addProductDetails(@RequestBody(required = true) final ProductInventory productInventory) {
        productInventoryService.addProductDetails(productInventory);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully.");
    }

    /**
     * Updates an existing product.
     *
     * @param productID - ProductID to be updated
     * @param productInventory The updated product.
     * @return A response entity indicating the status of the operation.
     */
    @Operation(operationId = "updateProductDetails", summary = "Update Product Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(path = "/{productID}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateProductDetails(
            @Parameter(description = "The ID of the product to update.", required = true)
            @PathVariable final Long productID, @RequestBody(required = true) final ProductInventory productInventory) {
        productInventoryService.updateProductDetails(productID, productInventory);
        return ResponseEntity.ok("Product updated successfully.");
    }

    /**
     * Deletes a product by ID.
     *
     * @param productID The ID of the product to delete.
     * @return A response entity indicating the status of the operation.
     */
    @Operation(operationId = "deleteProduct", summary = "Delete Product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(path = "/{productID}")
    public ResponseEntity<String> deleteProductDetails(
            @Parameter(description = "The ID of the product to delete.", required = true)
            @PathVariable final Long productID) {
        productInventoryService.deleteProductDetails(productID);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
