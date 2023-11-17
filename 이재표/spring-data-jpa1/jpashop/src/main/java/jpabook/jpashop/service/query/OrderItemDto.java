package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;

@Getter
public class OrderItemDto {
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
}