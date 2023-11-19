package jpashop.realspringjpa2.api;

import java.util.List;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.domain.OrderItem;
import jpashop.realspringjpa2.repository.OrderRepository;
import jpashop.realspringjpa2.repository.dto.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

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
}
