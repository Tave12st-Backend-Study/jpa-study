package jpabook;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS") //DB 중에 order가 안되는 이름이 있다.
public class Orders {
    @Id
    @GeneratedValue
    @Column(name = "ORDERS_ID")
    private Long id;
    @OneToOne
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;
    private LocalDateTime orderDate;
    private String status;
}
