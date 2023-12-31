package jpabook_jinu.jpashop_jinu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook_jinu.jpashop_jinu.domain.item.Item;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;





}
