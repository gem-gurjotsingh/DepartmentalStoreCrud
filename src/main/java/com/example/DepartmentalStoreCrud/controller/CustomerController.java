package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/customerDetails")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(operationId = "getAllCustomers", summary = "Get all Customers")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(operationId = "addCustomerDetails", summary = "Add Customer Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<String> addCustomerDetails(@RequestBody Customer customer) {
        customerService.addCustomerDetails(customer);
        return ResponseEntity.ok("Customer added successfully.");
    }

    @Operation(operationId = "updateCustomer", summary = "Update Customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{customerId}")
     public ResponseEntity<String> updateCustomerDetails(@PathVariable Long customerID, @RequestBody Customer customer) {
        customerService.updateCustomerDetails(customer);
        return ResponseEntity.ok("Customer updated successfully.");
    }

    @Operation(operationId = "deleteCustomer", summary = "Delete Customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{customerID}")
    public ResponseEntity<String> deleteCustomerDetails(@PathVariable Long customerID) {
        customerService.deleteCustomerDetails(customerID);
        return ResponseEntity.ok("Customer deleted successfully.");
    }
}