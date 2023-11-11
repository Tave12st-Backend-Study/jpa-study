package jpabook.jpashop01.api;

import jpabook.jpashop01.domain.Order;
import jpabook.jpashop01.repository.OrderRepository;
import jpabook.jpashop01.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * XToONE
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            order.getMember().getName(); //lazy 강제 초기화
            order.getDelivery().getAddress();
        }
        return all;
    }
}
