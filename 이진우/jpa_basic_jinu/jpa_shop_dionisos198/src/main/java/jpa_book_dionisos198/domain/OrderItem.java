package jpa_book_dionisos198.domain;

import javax.persistence.*;

@Entity
public class OrderItem extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /* @Column(name = "ORDER_ID")
        private Long orderId;*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
//    @Column(name = "ITEM_ID")
//    private Long itemId;

    public Long getId() {
        return id;
    }

   /* public Long getOrderId() {
        return orderId;
    }

    public Long getItemId() {
        return itemId;
    }*/

    public int getOrderPrice() {
        return orderPrice;
    }

    public int getCount() {
        return count;
    }

    private int orderPrice;

    private int count;

    public void setId(Long id) {
        this.id = id;
    }

   /* public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }*/

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OrderItem() {
    }
}
