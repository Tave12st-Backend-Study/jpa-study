package jpashop.realspringjpa2.api.dto;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpashop.realspringjpa2.domain.Address;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    // 속에있는 모든 엔티티를 그대로 반환하지 말것, Address같은 ValueObject는 괜찮음
    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        // order.getOrderItems().stream().forEach(o -> o.getItem().getName());   // proxy lazy 강제 초기화
        List<OrderItemDto> collectItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(toList());
        orderItems = collectItems;
    }
}
