package com.example.DepartmentalStoreCrud.repository;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.bean.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BackorderRepository extends JpaRepository<Backorder, Long> {
   Backorder findByOrder(Order order);

}
