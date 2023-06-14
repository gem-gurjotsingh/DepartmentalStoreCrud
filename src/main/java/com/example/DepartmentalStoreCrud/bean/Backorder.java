package com.example.DepartmentalStoreCrud.bean;
import javax.persistence.*;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Data;

@Entity
@Data
@Table(name = "Backorder")
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public class Backorder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backorderID;

    @OneToOne
    @JoinColumn(name = "orderID", referencedColumnName = "orderID")
    private Order order;
}
