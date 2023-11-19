package jpashop.realspringjpa2.api;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.List;
import jpashop.realspringjpa2.api.dto.OrderDto;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.repository.OrderRepository;
import jpashop.realspringjpa2.repository.query.OrderFlatDto;
import jpashop.realspringjpa2.repository.query.OrderItemQueryDto;
import jpashop.realspringjpa2.repository.query.OrderQueryDto;
import jpashop.realspringjpa2.repository.query.OrderQueryRepository;
import jpashop.realspringjpa2.service.query.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository queryRepository;
    private final OrderQueryService queryService;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        return queryService.ordersV1();
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return queryService.ordersV2();
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        return queryService.ordersV3();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        return queryService.ordersV3_page(offset, limit);
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

}
