package com.example.DepartmentalStoreCrud.bean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name="Orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
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
}