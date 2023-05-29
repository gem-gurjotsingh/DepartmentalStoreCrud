package com.example.DepartmentalStoreCrud.bean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="ProductInventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private Long productID;

    @Column(name = "productDesc")
    private String productDesc;

    @Column(name = "productName")
    private String productName;

    @Column(name = "price")
    private double price;

    @Column(name = "expiry")
    private Date expiry;

    @Column(name = "count")
    private int count;

    @Column(name = "availability")
    private boolean availability;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "productID")
    private List<Order> orders = new ArrayList<>();
}