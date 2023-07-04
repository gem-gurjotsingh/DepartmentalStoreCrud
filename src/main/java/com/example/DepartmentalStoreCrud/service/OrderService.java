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

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(Long orderID) {
        return orderRepo.findById(orderID)
                .orElseThrow(() -> new NoSuchElementException("No order exists with ID: " + orderID));
    }

    public void updateOtherEntities(Order order) {
        Customer customer = customerRepo.findById(order.getCustomer().getCustomerID()).orElse(null);
        ProductInventory productInventory = productInventoryRepo.findById(order.getProductInventory().getProductID()).orElse(null);
        order.setCustomer(customer);
        order.setProductInventory(productInventory);
    }

    public void applyDiscount(Order order){
        ProductInventory productInventory = order.getProductInventory();
        double totalPrice = order.getOrderQuantity() * productInventory.getPrice();
        order.setTotalPrice(totalPrice);
        double discountedPrice = order.getTotalPrice() - order.getTotalPrice() * (order.getDiscount() / 100.0);
        order.setDiscountedPrice(discountedPrice);
    }

    public void checkProductAvail(Order order){
        ProductInventory productInventory = order.getProductInventory();
        if(productInventory.getProductQuantity() > order.getOrderQuantity()) {
            orderRepo.save(order);
            productInventory.setProductQuantity(productInventory.getProductQuantity() - order.getOrderQuantity());
            productInventoryRepo.save(productInventory);
        }
        else {
            Order savedOrder = orderRepo.save(order);
            // Create a backorder for the order
            Backorder backorder = new Backorder();
            backorder.setOrder(savedOrder);
            backorderService.createBackorder(backorder);
            throw new IllegalStateException("Order placed successfully but out of stock. We will notify you once it is in stock");
        }
    }

    public void addOrderDetails(Order order) {
        updateOtherEntities(order);
        orderRepo.save(order);
        applyDiscount(order);
        checkProductAvail(order);
    }

    public void updateOrderDetails(Order order) {
        updateOtherEntities(order);
        orderRepo.save(order);
        checkProductAvail(order);
    }

    public void deleteOrderDetails(Long orderID) {
        Order savedOrder = getOrderById(orderID);
        ProductInventory productInventory = savedOrder.getProductInventory();

        if(productInventory.getProductQuantity() > 0)
        productInventory.setProductQuantity(productInventory.getProductQuantity() + savedOrder.getOrderQuantity());

        Backorder backorder = backorderRepo.findByOrder(savedOrder);
        if (backorder != null) {
            backorderService.deleteBackorder(backorder.getBackorderID());
        }
        orderRepo.deleteById(orderID);
    }
}
