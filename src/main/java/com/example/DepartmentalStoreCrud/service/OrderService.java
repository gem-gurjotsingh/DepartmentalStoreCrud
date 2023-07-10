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

    public final List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public final Order getOrderById(final Long orderID) {
        return orderRepo.findById(orderID)
                .orElseThrow(() -> new NoSuchElementException("No order exists with ID: " + orderID));
    }

    private void fetchOtherEntities(final Order order) {
        Long customerID = order.getCustomer().getCustomerID();
        Long productID = order.getProductInventory().getProductID();
        Customer customer = customerRepo.findById(customerID)
                .orElseThrow(() -> new NoSuchElementException("No customer exists with ID: " + order.getCustomer().getCustomerID()));
        ProductInventory productInventory = productInventoryRepo.findById(productID)
                .orElseThrow(() -> new NoSuchElementException("No product exists with ID: " + order.getProductInventory().getProductID()));
        order.setCustomer(customer);
        order.setProductInventory(productInventory);
        orderRepo.save(order);
    }

    private void applyDiscount(final Order order) {
        ProductInventory productInventory = order.getProductInventory();
        double totalPrice = order.getOrderQuantity() * productInventory.getPrice();
        order.setTotalPrice(totalPrice);
        double discountedPrice = order.getTotalPrice() - order.getTotalPrice() * (order.getDiscount() / 100.0);
        order.setDiscountedPrice(discountedPrice);
    }

    private void checkIfBackorder(final Order order) {
        ProductInventory productInventory = order.getProductInventory();
        if (productInventory.getProductQuantity() >= order.getOrderQuantity()) {
            orderRepo.save(order);
            productInventory.setProductQuantity(productInventory.getProductQuantity() - order.getOrderQuantity());
            productInventoryRepo.save(productInventory);
        } else {
            Order savedOrder = orderRepo.save(order);
            // Create a backorder for the order
            Backorder backorder = new Backorder();
            backorder.setOrder(savedOrder);
            backorderService.createBackorder(backorder);
            throw new IllegalStateException("Order placed successfully as a backorder");
        }
    }

    public final void addOrderDetails(final Order order) {
        fetchOtherEntities(order);
        applyDiscount(order);
        checkIfBackorder(order);
    }

    public final void updateOrderDetails(final Order order) {
        fetchOtherEntities(order);
        checkIfBackorder(order);
    }

    public final void deleteOrderDetails(final Long orderID) {
        Order savedOrder = getOrderById(orderID);
        ProductInventory productInventory = savedOrder.getProductInventory();

        Backorder backorder = backorderRepo.findByOrder(savedOrder);
        if (backorder != null) {
            backorderService.deleteBackorder(backorder.getBackorderID());
        } else {
            productInventory.setProductQuantity(productInventory.getProductQuantity() + savedOrder.getOrderQuantity());
        }
        orderRepo.deleteById(orderID);
    }
}
