package jpashop.realspringjpa2.api.dto;

import java.time.LocalDateTime;
import jpashop.realspringjpa2.domain.Address;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderSimpleDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleDto(Order order) {
        orderId = order.getId();
        name =  order.getMember().getName();    // LAZY 초기화
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress(); // LAZY 초기화
    }
}
