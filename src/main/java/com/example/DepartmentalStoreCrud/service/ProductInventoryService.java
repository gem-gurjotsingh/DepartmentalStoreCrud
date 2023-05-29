package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInventoryService {

    @Autowired
    private ProductInventoryRepository productRepo;

    public List<ProductInventory> getAllProducts()
    {
        return productRepo.findAll();
    }

    public void addProductDetails(ProductInventory productInventory) {
        productRepo.save(productInventory);
    }

    public void updateProductDetails(ProductInventory productInventory) {
        productRepo.save(productInventory);
    }

    public void deleteProductDetails(Long orderID) {
        productRepo.deleteById(orderID);
    }
}