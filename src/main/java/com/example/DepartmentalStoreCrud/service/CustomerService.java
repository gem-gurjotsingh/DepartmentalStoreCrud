package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customRepo;

    public List<Customer> getAllCustomers()
    {
        return customRepo.findAll();
    }

    public void addCustomerDetails(Customer customer) {
        customRepo.save(customer);
    }

    public void updateCustomerDetails(Customer customer) {
        customRepo.save(customer);
    }

    public void deleteCustomerDetails(Long customerID) {
        customRepo.deleteById(customerID);
    }
}