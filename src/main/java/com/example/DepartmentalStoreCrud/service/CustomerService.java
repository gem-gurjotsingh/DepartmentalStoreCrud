package com.example.DepartmentalStoreCrud.service;
import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.List;

@Service
@Data
public class CustomerService {

    @Value("${email.regexp}")
    private String emailRegexp;

    @Value("${contact.regexp}")
    private String contactRegexp;

    @Autowired
    private CustomerRepository customRepo;

    @Autowired
    private OrderRepository orderRepo;

    public final List<Customer> getAllCustomers() {
        return customRepo.findAll();
    }

    public final Customer getCustomerById(final Long customerID) {
        return customRepo.findById(customerID)
                .orElseThrow(() -> new NoSuchElementException("No customer exists with ID: " + customerID));
    }

    public final List<Order> getOrdersByCustomer(final Long customerID) {
        if (getCustomerById(customerID) != null) {
            return orderRepo.findByCustomer_CustomerID(customerID);
        }
        return null;
    }

    private void validateContact(final String contact) {
        if (!contact.matches(contactRegexp)) {
            throw new IllegalArgumentException("Invalid country code or contact number");
        }
    }

    private void validateEmail(final String email) {
        if (!email.matches(emailRegexp)) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public final void addCustomerDetails(final Customer customer) {
        validateContact(customer.getContactNumber());
        validateEmail(customer.getEmailID());
        customRepo.save(customer);
    }

    public final void updateCustomerDetails(final Long customerID, final Customer customer) {
        Customer existingCustomer = getCustomerById(customerID);
        if (existingCustomer != null) {
            existingCustomer.setCustomerID(customerID);
            existingCustomer.setFullName(customer.getFullName());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setContactNumber(customer.getContactNumber());
            existingCustomer.setEmailID(customer.getEmailID());
        } else {
            throw new NoSuchElementException("No customer exists with ID: " + customerID);
        }
        validateContact(existingCustomer.getContactNumber());
        validateEmail(existingCustomer.getEmailID());
        customRepo.save(existingCustomer);
    }

    public final void deleteCustomerDetails(final Long customerID) {
        if (getCustomerById(customerID) != null) {
            customRepo.deleteById(customerID);
        }
    }
}
