package jpabook_jinu.jpashop_jinu.api;

import jpabook_jinu.jpashop_jinu.domain.*;
import jpabook_jinu.jpashop_jinu.repository.OrderRepository;
import jpabook_jinu.jpashop_jinu.repository.order.query.OrderFlatDto;
import jpabook_jinu.jpashop_jinu.repository.order.query.OrderItemQueryDto;
import jpabook_jinu.jpashop_jinu.repository.order.query.OrderQueryDto;
import jpabook_jinu.jpashop_jinu.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractCachedDomainDataAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all=orderRepository.findAll(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems=order.getOrderItems();
            orderItems.stream().forEach(o->o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders=orderRepository.findAll(new OrderSearch());
        List<OrderDto> collect=orders.stream()
                .map(o->new OrderDto(o))
                .collect(toList());
        return collect;

    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders=orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref="+order+" id="+order.getId());
        }
        List<OrderDto> collect=orders.stream()
                .map(o->new OrderDto(o))
                .collect(toList());
        return collect;

    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset",defaultValue = "0") int offset, @RequestParam(value = "limit",defaultValue = "100")int limit){
        List<Order> orders=orderRepository.findAllWithMemberDelivery(offset,limit);


        List<OrderDto> result=orders.stream()
                .map(o->new OrderDto(o))
                .collect(toList());
        return result;

    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }


    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order){
            orderId=order.getId();
            name=order.getMember().getName();
            orderDate=order.getOrderDate();
            orderStatus=order.getOrderStatus();
            address=order.getDelivery().getAddress();
            order.getOrderItems().stream().forEach(o->o.getItem().getName());
            orderItems=order.getOrderItems().stream()
                    .map(orderItem-> new OrderItemDto(orderItem))
                    .collect(toList());
        }

    }
    @Data
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            itemName=orderItem.getItem().getName();
            orderPrice=orderItem.getOrderPrice();
            count=orderItem.getCount();
        }
    }
}
