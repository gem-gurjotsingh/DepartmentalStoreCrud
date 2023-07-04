package com.example.DepartmentalStoreCrud.service;
import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {

    @Value("${email.regexp}")
    private String emailRegexp;

    @Value("${contact.regexp}")
    private String contactRegexp;

    @Autowired
    private CustomerRepository customRepo;

    @Autowired
    private OrderRepository orderRepo;

    public List<Customer> getAllCustomers()
    {
        return customRepo.findAll();
    }

    public Customer getCustomerById(Long customerID) {
        return customRepo.findById(customerID)
                .orElseThrow(() -> new NoSuchElementException("No customer exists with ID: " + customerID));
    }

    public List<Order> getOrdersByCustomer(Long customerID) {
        Customer customer = getCustomerById(customerID);
        return orderRepo.findByCustomer_CustomerID(customerID);
    }

    private void validateContact(String contact){
        if(!contact.matches(contactRegexp)) {
            throw new IllegalArgumentException("Invalid country code or contact number");
        }
    }

    private void validateEmail(String email) {
        if (!email.matches(emailRegexp)) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public void addCustomerDetails(Customer customer) {
        validateContact(customer.getContactNumber());
        validateEmail(customer.getEmailID());
        customRepo.save(customer);
    }

    public void updateCustomerDetails(Long customerID, Customer customer) {
        Customer existingCustomer = getCustomerById(customerID);
        if(existingCustomer!=null) {
            existingCustomer.setCustomerID(customerID);
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setContactNumber(customer.getContactNumber());
            existingCustomer.setEmailID(customer.getEmailID());
        }
        validateContact(existingCustomer.getContactNumber());
        validateEmail(existingCustomer.getEmailID());
        customRepo.save(existingCustomer);
    }

    public void deleteCustomerDetails(Long customerID) {
        if(getCustomerById(customerID) != null)
            customRepo.deleteById(customerID);
    }
}
