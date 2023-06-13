package com.example.DepartmentalStoreCrud.service;
import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customRepo;

    public List<Customer> getAllCustomers()
    {
        return customRepo.findAll();
    }

    public void addCustomerDetails(Customer customer) {
        String validEmail = customer.getEmailID();
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(validEmail.matches(emailRegex)){
            customRepo.save(customer);
        }
        else{
            throw new IllegalArgumentException("Invalid email");
        }

    }

    public void updateCustomerDetails(Customer customer) {
        String validEmail = customer.getEmailID();
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if(validEmail.matches(emailRegex)){
            customRepo.save(customer);
        }
        else{
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void deleteCustomerDetails(Long customerID) {
        customRepo.deleteById(customerID);
    }
}