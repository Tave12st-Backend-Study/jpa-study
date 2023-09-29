package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class OrderItem extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

//    @Column(name = "ORDER_ID")
//    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

//    @Column(name = "ITEM_ID")
//    private Long itemId;

    private int orderPrice;
    private int count;
}
