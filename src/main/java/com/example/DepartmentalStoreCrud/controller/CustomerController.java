package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customerDetails")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customerDetails")
    public ResponseEntity<String> addCustomerDetails(@RequestBody Customer customer) {
        customerService.addCustomerDetails(customer);
        return ResponseEntity.ok("Customer added successfully.");
    }

    @PutMapping("/customerDetails/{customerID}")
    public ResponseEntity<String> updateCustomerDetails(@RequestBody Customer customer) {
        customerService.updateCustomerDetails(customer);
        return ResponseEntity.ok("Customer updated successfully.");
    }

    @DeleteMapping("/customerDetails/{customerID}")
    public ResponseEntity<String> deleteCustomerDetails(@PathVariable Long customerID) {
        customerService.deleteCustomerDetails(customerID);
        return ResponseEntity.ok("Customer deleted successfully.");
    }
}