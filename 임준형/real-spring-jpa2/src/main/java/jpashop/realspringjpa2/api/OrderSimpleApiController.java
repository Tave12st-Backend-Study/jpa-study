package jpashop.realspringjpa2.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpashop.realspringjpa2.domain.Address;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.domain.OrderStatus;
import jpashop.realspringjpa2.repository.OrderRepository;
import jpashop.realspringjpa2.repository.dto.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 컬렉션이 아닌, xToOne Order Order -> Member Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    // ORDER 2개
    // N + 1 -> 1 + 회원 N + 배송 N

    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    // fetch join
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        return result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name =  order.getMember().getName();    // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
