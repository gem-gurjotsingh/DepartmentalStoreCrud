package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.bean.Customer;
import com.example.DepartmentalStoreCrud.bean.Order;
import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.BackorderRepository;
import com.example.DepartmentalStoreCrud.repository.CustomerRepository;
import com.example.DepartmentalStoreCrud.repository.OrderRepository;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private BackorderService backorderService;

    @Autowired
    private ProductInventoryRepository productInventoryRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private BackorderRepository backorderRepo;

    public List<Order> getAllOrders()
    {
        return orderRepo.findAll();
    }

    public Order getOrderById(Long orderID) {
        return orderRepo.findById(orderID).orElseThrow(NoSuchElementException::new);
    }

    public void updateOtherEntities(Order order){
        Customer customer = customerRepo.findById(order.getCustomer().getCustomerID()).orElse(null);
        ProductInventory productInventory = productInventoryRepo.findById(order.getProductInventory().getProductID()).orElse(null);
        order.setCustomer(customer);
        order.setProductInventory(productInventory);
    }

    public void checkProductAvail(Order order){
        ProductInventory productInventory = order.getProductInventory();
        double discountedPrice = productInventory.getPrice() - productInventory.getPrice() * (order.getDiscount() / 100.0);
        order.setDiscountedPrice(discountedPrice);
        if(order.getProductInventory().getCount() > 0 && order.getProductInventory().isAvailability()) {
            orderRepo.save(order);
            productInventory.setCount(productInventory.getCount() - order.getQuantity());
            productInventoryRepo.save(productInventory);
        }
        else {
            Order savedOrder = orderRepo.save(order);
            // Create a backorder for the order
            Backorder backorder = new Backorder();
            backorder.setOrder(savedOrder);
            backorderService.createBackorder(order.getOrderID(), backorder);
            throw new IllegalStateException("Order placed successfully but out of stock. We will notify you once it is in stock");
        }
    }

    public void addOrderDetails(Order order) {
        updateOtherEntities(order);
        orderRepo.save(order);
        checkProductAvail(order);
    }

    public void updateOrderDetails(Order order) {
        updateOtherEntities(order);
        orderRepo.save(order);
        checkProductAvail(order);
    }

    public void deleteOrderDetails(Long orderID, Order order) {
        orderRepo.findById(orderID).orElseThrow(NoSuchElementException::new);
        Backorder backorder = backorderRepo.findByOrder(order);

        if (backorder != null) {
            backorderService.deleteBackorder(backorder.getBackorderID());
        }

        orderRepo.deleteById(orderID);
    }
}