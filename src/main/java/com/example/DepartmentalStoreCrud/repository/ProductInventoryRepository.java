package com.example.DepartmentalStoreCrud.repository;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {

}
