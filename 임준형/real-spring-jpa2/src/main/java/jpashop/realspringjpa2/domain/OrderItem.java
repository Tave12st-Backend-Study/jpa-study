package jpashop.realspringjpa2.domain;

import jpashop.realspringjpa2.item.Item;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량

    // == 생성 메서드 == //
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(orderPrice);
        // 재고 줄이기
        item.removeStock(count);
        return orderItem;
    }

    // == 비즈니스 로직 == //
    /**
     * 주문이 취소되었을 때 상품들의 수량이 다시 늘어나야함.
     */
    public void cancel() {
        getItem().addStock(count);
    }

    // 한 아이템의 주문에 대해 총 주문 결과를 구하는 메서드
    // 해당 주문 상품 전체 가격 조회 A book을 5개, B book을 2개 사면 각각 상품에 대해 구할때마다 메서드 호출됨
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
