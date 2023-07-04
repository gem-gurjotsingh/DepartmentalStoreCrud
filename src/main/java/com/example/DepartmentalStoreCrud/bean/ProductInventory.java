package com.example.DepartmentalStoreCrud.bean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "ProductInventory")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productID")
    private Long productID;

    @Column(name = "productDesc")
    private String productDesc;

    @Column(name = "productName")
    private String productName;

    @Column(name = "price")
    private double price;

    @Column(name = "productQuantity")
    private int productQuantity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "productID")
    private List<Order> orders = new ArrayList<>();
}
