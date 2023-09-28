package jpabook;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DELIVERY") //DB 중에 order가 안되는 이름이 있다.
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "DELIVERY_ID")
    private Long id;
    @OneToOne(mappedBy = "delivery")
    private Orders orders;
    private String city;
    private String street;
    private String zipcode;
    private DeliveryStatus status;
}
