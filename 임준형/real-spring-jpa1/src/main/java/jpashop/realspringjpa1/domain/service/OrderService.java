package jpashop.realspringjpa1.domain.service;

import java.util.List;
import jpashop.realspringjpa1.domain.Delivery;
import jpashop.realspringjpa1.domain.Member;
import jpashop.realspringjpa1.domain.Order;
import jpashop.realspringjpa1.domain.OrderItem;
import jpashop.realspringjpa1.domain.item.Item;
import jpashop.realspringjpa1.domain.repository.ItemRepository;
import jpashop.realspringjpa1.domain.repository.MemberRepository;
import jpashop.realspringjpa1.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member findMember = memberRepository.findOne(memberId);
        Item findItem = itemRepository.findOne(itemId);

        // 배송 정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(findMember, delivery, orderItem);

        // 주문 저장
        // cascade.ALL 로 설정해놨기 떄문에 delivery와 orderItem을 새로 persist 할 필요가 없다.
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order findOrder = orderRepository.findOne(orderId);
        // 주문 취소
        findOrder.cancel();
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(order());
//    }
}
