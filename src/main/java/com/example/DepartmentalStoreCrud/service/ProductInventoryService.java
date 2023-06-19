package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.ProductInventory;
import com.example.DepartmentalStoreCrud.repository.ProductInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductInventoryService {

    @Autowired
    private ProductInventoryRepository productRepo;

    public List<ProductInventory> getAllProducts()
    {
        return productRepo.findAll();
    }

    public ProductInventory getProductById(Long productID) {
        return productRepo.findById(productID).orElseThrow(NoSuchElementException::new);
    }
    public void addProductDetails(ProductInventory productInventory) {
        productRepo.save(productInventory);
    }

    public void updateProductDetails(Long productID, ProductInventory productInventory) {
        ProductInventory existingProduct = getProductById(productID);
        if(existingProduct != null) {
            existingProduct.setProductID(productID);
            existingProduct.setProductDesc(productInventory.getProductDesc());
            existingProduct.setProductName(productInventory.getProductName());
            existingProduct.setPrice(productInventory.getPrice());
            existingProduct.setExpiry(productInventory.getExpiry());
            existingProduct.setCount(productInventory.getCount());
            existingProduct.setAvailability(productInventory.isAvailability());
        }
        productRepo.save(existingProduct);
    }

    public void deleteProductDetails(Long productID) {
        if(getProductById(productID) != null)
        productRepo.deleteById(productID);
    }
}
