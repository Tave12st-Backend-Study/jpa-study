package jpabook_jinu.jpashop_jinu.api;

import jpabook_jinu.jpashop_jinu.domain.Order;
import jpabook_jinu.jpashop_jinu.domain.OrderItem;
import jpabook_jinu.jpashop_jinu.domain.OrderSearch;
import jpabook_jinu.jpashop_jinu.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
