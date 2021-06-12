package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "promotion")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Promotion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sale_percent", nullable = false)
    private int salePercent;

    @Column(name = "description", nullable = false
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String description;

    @OneToMany(mappedBy = "promotion")
    private List<Order> listOrders;

}
