package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.service.ProductInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/productDetails")
public class ProductInventoryController {

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
    @GetMapping
    public List<ProductInventory> getAllProducts() {
        return productInventoryService.getAllProducts();
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
    @GetMapping("/{productID}")
    public ResponseEntity<ProductInventory> getProductById(
            @Parameter(description = "The ID of the product to retrieve.", required = true)
            @PathVariable final Long productID) {
        return ResponseEntity.ok(productInventoryService.getProductById(productID));
    }

    /**
     * Uploads an Excel file containing product details and saves the data to the database.
     *
     * @param file The Excel file to upload.
     * @return A response entity indicating the status of the operation.
     */
    @Operation(operationId = "addProductDetailsViaExcel", summary = "Add Product Details Via Excel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel file uploaded to db"),
            @ApiResponse(responseCode = "400", description = "Invalid file format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadProducts(@Parameter(description = "The Excel file to upload.", required = true)
           @RequestParam("file") final MultipartFile file) throws IOException {
        productInventoryService.saveExcel(file);
        return ResponseEntity.ok("Products added to database");
    }
//    public ResponseEntity<String> addProductDetails(@RequestBody(required = true) ProductInventory productInventory) {
//        productInventoryService.addProductDetails(productInventory);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully.");
//    }

    /**
     * Updates an existing product.
     *
     * @param productInventory The updated product.
     * @return A response entity indicating the status of the operation.
     */
    @Operation(operationId = "updateProductDetails", summary = "Update Product Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{productID}")
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
    @DeleteMapping("/{productID}")
    public ResponseEntity<String> deleteProductDetails(
            @Parameter(description = "The ID of the product to delete.", required = true)
            @PathVariable final Long productID) {
        productInventoryService.deleteProductDetails(productID);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
