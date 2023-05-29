package com.example.DepartmentalStoreCrud.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Backorder")
public class Backorder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backorderID;

    @OneToOne
    @JoinColumn(name = "orderID", referencedColumnName = "orderID")
    private Order order;
}