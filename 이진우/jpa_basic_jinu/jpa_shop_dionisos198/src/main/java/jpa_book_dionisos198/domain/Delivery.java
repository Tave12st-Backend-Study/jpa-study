package jpa_book_dionisos198.domain;

import javax.persistence.*;

@Entity
public class Delivery extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private DeliveryStatus deliveryStatus;
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;
}
