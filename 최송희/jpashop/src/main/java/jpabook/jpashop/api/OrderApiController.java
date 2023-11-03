package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        //객체 그래프 초기화
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            //리스트 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
        }
        //엔티티가 직접 노출되는 문제가 있다. -> DTO로 바꿔서 응답해보자(v2)
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());//select order all(1)
        List<OrderDto> collect = orders.stream().map(OrderDto::new).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream().map(OrderDto::new).collect(Collectors.toList());
        return collect;
    }

    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;//value object는 노출해도 된다.
//        private List<OrderItem> orderItems; //엔티티에 대한 의존을 완전히 끊어내야 한다.(굉장히 많은 실수가 여기에 있다)
        private List<OrderItemDto> orderItemDtos;

        public OrderDto(Order o) {
            this.orderId = o.getId();
            this.name = o.getMember().getName();//select member(1) select member(1)
            this.orderDate = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.address = o.getMember().getAddress();
            o.getOrderItems().forEach(item->item.getItem().getName()); //select item all (2) select item all(2)
            this.orderItemDtos = o.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
        }

    }

    @Getter
    static class OrderItemDto{
        private String itemName; //상품명
        private int orderPrice; //상품가격
        private int count; //상품 수량

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
