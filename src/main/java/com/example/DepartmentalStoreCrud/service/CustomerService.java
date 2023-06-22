package com.example.DepartmentalStoreCrud.service;
import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customRepo;

    @Autowired
    private OrderRepository orderRepo;

    public List<Customer> getAllCustomers()
    {
        return customRepo.findAll();
    }

    public Customer getCustomerById(Long customerID) {
        return customRepo.findById(customerID).orElseThrow(NoSuchElementException::new);
    }

    public List<Order> getOrdersByCustomer(Long customerID) {
        Customer customer = getCustomerById(customerID);
        return orderRepo.findByCustomer_CustomerID(customerID);
    }

    private void validateEmail(String email) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void addCustomerDetails(Customer customer) {
        validateEmail(customer.getEmailID());
        customRepo.save(customer);
    }

    public void updateCustomerDetails(Long customerID, Customer customer) {
        Customer existingCustomer = getCustomerById(customerID);
        if(existingCustomer != null) {
            existingCustomer.setCustomerID(customerID);
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setContactNumber(customer.getContactNumber());
            existingCustomer.setEmailID(customer.getEmailID());
        }
        validateEmail(existingCustomer.getEmailID());
        customRepo.save(existingCustomer);
    }

    public void deleteCustomerDetails(Long customerID) {
        if(getCustomerById(customerID) != null)
            customRepo.deleteById(customerID);
    }
}
