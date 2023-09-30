package com.example.jpashop;

import javax.persistence.*;

@Entity
public class Delivery extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zip;
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery")
    private Order order;
}
