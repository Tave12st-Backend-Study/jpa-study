package jpabook.jpashop.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Delivery {
    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;
    private DeliveryStatus status;
}
