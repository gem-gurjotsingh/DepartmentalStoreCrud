package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.repository.BackorderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BackorderService {

    @Autowired
    private BackorderRepository backorderRepository;

    public List<Backorder> getAllBackorders() {
        return backorderRepository.findAll();
    }

    public Backorder getBackorderById(Long backorderId) {
        return backorderRepository.findById(backorderId).orElseThrow(NoSuchElementException::new);
    }

    public void createBackorder(Long orderID, Backorder backorder) {
        backorderRepository.save(backorder);
    }

    public void deleteBackorder(Long backorderId) {
        backorderRepository.deleteById(backorderId);
    }
}