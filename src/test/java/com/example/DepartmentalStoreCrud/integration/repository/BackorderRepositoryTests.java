package com.example.DepartmentalStoreCrud.integration.repository;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.repository.BackorderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BackorderRepositoryTests {

    @Autowired
    private BackorderRepository backorderRepository;

    @Test
    void testGetAllBackorders() {
        List<Backorder> backorderList = new ArrayList<>();
        backorderList.add(createBackorder(1L));
        backorderList.add(createBackorder(2L));
        backorderRepository.saveAll(backorderList);
        assertEquals(2, backorderRepository.findAll().size());
    }

    @Test
    void testGetBackorderById() {
        Backorder backorder = createBackorder(1L);
        backorderRepository.save(backorder);
        Backorder findBackorder = backorderRepository.findById(1L).orElse(null);
        assertNotNull(findBackorder);
    }

    @Test
    void testDeleteBackorderById() {
        Backorder backorder = createBackorder(1L);
        backorderRepository.save(backorder);
        backorderRepository.deleteById(1L);
        assertThat(backorderRepository.findAll().isEmpty());
    }

    private Backorder createBackorder(Long id) {
        Backorder backorder = new Backorder();
        backorder.setBackorderID(id);
        backorder.setOrder(null);
        return backorder;
    }
}
