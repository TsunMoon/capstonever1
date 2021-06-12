package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import java.util.Date;
import java.util.List;

@Entity(name = "PriceByDate")
@Table(name = "price_by_date")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceByDate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "createAt", nullable = false)
    private Date createAt;

    @Column(name = "isUsed", nullable = false)
    private boolean isUsed;

    @Column(name = "updateAt")
    private Date updateAt;

    @ManyToOne
    @JoinColumn(name = "service_component_id")
    private ServiceComponent serviceComponent;

    @JsonIgnore
    @OneToMany(mappedBy = "priceByDate")
    private List<OrderDetail> listOrderDetails;

}
