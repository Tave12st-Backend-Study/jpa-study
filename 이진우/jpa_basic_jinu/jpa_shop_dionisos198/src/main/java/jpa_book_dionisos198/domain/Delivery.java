package jpa_book_dionisos198.domain;

import javax.persistence.*;

@Entity
public class Delivery extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;

    private DeliveryStatus deliveryStatus;
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;
}
