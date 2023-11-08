package jpabook_jinu.jpashop_jinu.api;

import jpabook_jinu.jpashop_jinu.domain.Address;
import jpabook_jinu.jpashop_jinu.domain.Order;
import jpabook_jinu.jpashop_jinu.domain.OrderSearch;
import jpabook_jinu.jpashop_jinu.domain.OrderStatus;
import jpabook_jinu.jpashop_jinu.repository.OrderRepository;
import jpabook_jinu.jpashop_jinu.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook_jinu.jpashop_jinu.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractCachedDomainDataAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all=orderRepository.findAll(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders=orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> result=orders.stream().map(o->new SimpleOrderDto(o)).collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders=orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result=orders.stream().map(o->new SimpleOrderDto(o)).collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress();
        }
    }
}
