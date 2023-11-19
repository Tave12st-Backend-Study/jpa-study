package jpashop.realspringjpa2.api;

import java.util.List;
import java.util.stream.Collectors;
import jpashop.realspringjpa2.api.dto.OrderSimpleDto;
import jpashop.realspringjpa2.api.dto.OrderSimpleQueryDto;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.repository.OrderRepository;
import jpashop.realspringjpa2.repository.query.OrderSimpleQueryRepository;
import jpashop.realspringjpa2.repository.dto.OrderSearch;
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
    private final OrderSimpleQueryRepository queryRepository;

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

    public List<OrderSimpleDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleDto> collect = orders.stream()
                .map(OrderSimpleDto::new)
                .collect(Collectors.toList());
        return collect;
    }

    // fetch join
    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleDto> result = orders.stream()
                .map(OrderSimpleDto::new)
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return queryRepository.findOrderDtos();
    }

}
