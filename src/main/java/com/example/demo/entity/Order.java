package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "Order")
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "createAt")
    private Date createAt;


    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerInfo customerInfo;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffInfo staffInfo;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "total")
    private Float Total;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> listOrderDetails;


}
