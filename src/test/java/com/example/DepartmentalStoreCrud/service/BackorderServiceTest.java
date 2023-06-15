package com.example.DepartmentalStoreCrud.service;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.repository.BackorderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BackorderServiceTest {

    @Mock
    private BackorderRepository backorderRepository;

    @InjectMocks
    private BackorderService backorderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBackorders() {
        // Arrange
        List<Backorder> backorders = new ArrayList<>();
        backorders.add(new Backorder());
        backorders.add(new Backorder());
        when(backorderRepository.findAll()).thenReturn(backorders);

        // Act
        List<Backorder> result = backorderService.getAllBackorders();

        // Assert
        assertEquals(backorders, result);
        verify(backorderRepository, times(1)).findAll();
    }

    @Test
    public void testGetBackorderById() {
        // Arrange
        Long backorderId = 1L;
        Backorder backorder = new Backorder();
        backorder.setBackorderID(backorderId);
        when(backorderRepository.findById(backorderId)).thenReturn(Optional.of(backorder));

        // Act
        Backorder result = backorderService.getBackorderById(backorderId);

        // Assert
        assertEquals(backorder, result);
        verify(backorderRepository, times(1)).findById(backorderId);
    }

    @Test
    public void testGetBackorderById_NonexistentId() {
        // Arrange
        Long backorderId = 1L;
        when(backorderRepository.findById(backorderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> backorderService.getBackorderById(backorderId));
        verify(backorderRepository, times(1)).findById(backorderId);
    }

    @Test
    public void testCreateBackorder() {
        // Arrange
        Backorder backorder = new Backorder();

        // Act
        backorderService.createBackorder(backorder);

        // Assert
        verify(backorderRepository, times(1)).save(backorder);
    }

    @Test
    public void testDeleteBackorder() {
        // Arrange
        Long backorderId = 1L;

        // Act
        backorderService.deleteBackorder(backorderId);

        // Assert
        verify(backorderRepository, times(1)).deleteById(backorderId);
    }
}