package jpashop.realspringjpa2.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import jpashop.realspringjpa2.domain.Address;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.domain.OrderItem;
import jpashop.realspringjpa2.domain.OrderStatus;
import jpashop.realspringjpa2.repository.OrderRepository;
import jpashop.realspringjpa2.repository.dto.OrderSearch;
import jpashop.realspringjpa2.repository.query.OrderFlatDto;
import jpashop.realspringjpa2.repository.query.OrderItemQueryDto;
import jpashop.realspringjpa2.repository.query.OrderQueryDto;
import jpashop.realspringjpa2.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository queryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        // @JsonIgnore를 붙여야하지만 생략. 어차피 이렇게 사용하지 않음
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        all.forEach(o -> {
            o.getMember().getName();
            o.getDelivery().getAddress();
            List<OrderItem> orderItems = o.getOrderItems(); // 강제 초기화
            orderItems.forEach(io -> io.getItem().getName()); // 강제 초기화
        });
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(toList());
        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());

        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());

        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return queryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return queryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    // 페이징 불가
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = queryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(),
                                o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(),
                                o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(),
                        e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

    @Data
    static class OrderDto {
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

    @Getter
    static class OrderItemDto {
        private String itemName; // 상품명
        private int orderPrice; // 주문 가격
        private int count;      // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
