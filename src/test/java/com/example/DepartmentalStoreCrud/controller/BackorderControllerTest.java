package com.example.DepartmentalStoreCrud.controller;

import com.example.DepartmentalStoreCrud.bean.Backorder;
import com.example.DepartmentalStoreCrud.service.BackorderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = AutoConfigureMockMvc.class)
@WebMvcTest(BackorderController.class)
public class BackorderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BackorderService backorderService;

    @InjectMocks
    private BackorderController backorderController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(backorderController).build();
    }

    @Test
    void createBackorderTest() throws Exception {
        Backorder backorder = createBackorder();

        mockMvc.perform(post("/backorders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(backorder)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Backorder created successfully."));

        verify(backorderService, never()).createBackorder(backorder);
    }

    @Test
    void getAllBackordersTest() throws Exception {
        List<Backorder> backorders = new ArrayList<>();
        backorders.add(createBackorder());

        when(backorderService.getAllBackorders()).thenReturn(backorders);

        mockMvc.perform(get("/backorders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(backorders.size())));

        verify(backorderService, times(1)).getAllBackorders();
    }

    @Test
    void getBackorderByIdTest_BackorderFound() throws Exception {
        Long backorderId = 1L;
        Backorder backorder = createBackorder();
        when(backorderService.getBackorderById(backorderId)).thenReturn(backorder);

        mockMvc.perform(get("/backorders/{backorderId}", backorderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(backorderService, times(1)).getBackorderById(backorderId);
    }

    @Test
    void deleteBackorderTest() throws Exception {
        Long backorderId = 1L;
        doNothing().when(backorderService).deleteBackorder(backorderId);

        mockMvc.perform(delete("/backorders/{backorderId}", backorderId))
                .andExpect(status().isOk());

        verify(backorderService, times(1)).deleteBackorder(backorderId);
    }

    private Backorder createBackorder() {
        Backorder backorder = new Backorder();
        backorder.setBackorderID(1L);
        backorder.setOrder(null);
        return backorder;
    }
}
