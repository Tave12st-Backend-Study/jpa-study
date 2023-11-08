package jpabook_jinu.jpashop_jinu.api;

import jpabook_jinu.jpashop_jinu.domain.*;
import jpabook_jinu.jpashop_jinu.repository.OrderRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractCachedDomainDataAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all=orderRepository.findAllByString(new OrderSearch());

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
        List<Order> orders=orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect=orders.stream()
                .map(o->new OrderDto(o))
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
        return collect;

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
                    .collect(Collectors.toList());
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
