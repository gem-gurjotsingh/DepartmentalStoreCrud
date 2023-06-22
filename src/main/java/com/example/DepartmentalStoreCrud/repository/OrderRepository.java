package com.example.DepartmentalStoreCrud.repository;

import com.example.DepartmentalStoreCrud.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByCustomer_CustomerID(Long customerID);
}