package jpa_basic_dionisos198;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MemberProduct {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int orderAmount;
    private Date orderDate;
}
