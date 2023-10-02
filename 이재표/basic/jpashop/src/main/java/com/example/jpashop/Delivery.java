package com.example.jpashop;

import javax.persistence.*;

@Entity
public class Delivery extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Address address;
    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;
}
