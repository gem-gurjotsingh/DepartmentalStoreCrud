package com.example.DepartmentalStoreCrud.bean;
import javax.persistence.*;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="Orders")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderID")
    private Long orderID;

    @ManyToOne
    @JoinColumn(name = "productID")
    private ProductInventory productInventory;

    @ManyToOne
    @JoinColumn(name = "customerID")
    private Customer customer;

    @Column(name = "orderTimestamp")
    private LocalDateTime orderTimestamp;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")
    private double discount;

    @Column(name="discountedPrice")
    private double discountedPrice;

    @Column(name="totalPrice")
    private double totalPrice;
}