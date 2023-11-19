package jpashop.realspringjpa2.service.query;

import static java.util.stream.Collectors.toList;

import java.util.List;
import jpashop.realspringjpa2.api.dto.OrderDto;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.domain.OrderItem;
import jpashop.realspringjpa2.repository.OrderRepository;
import jpashop.realspringjpa2.repository.dto.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;

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

    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(OrderDto::new)
                .collect(toList());
        return collect;
    }

    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());

        return result;
    }

    public List<OrderDto> ordersV3_page(int offset, int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());
        return result;
    }

}
