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

    public final List<Backorder> getAllBackorders() {
        return backorderRepository.findAll();
    }

    public final Backorder getBackorderById(final Long backorderId) {
        return backorderRepository.findById(backorderId)
                .orElseThrow(() -> new NoSuchElementException("No backorder exists with ID: " + backorderId));
    }

    public final void createBackorder(final Backorder backorder) {
        backorderRepository.save(backorder);
    }

    public final void deleteBackorder(final Long backorderId) {
        backorderRepository.deleteById(backorderId);
    }
}
