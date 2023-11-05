package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    /*주문*/
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        /*OrderItem orderItem = new OrderItem();로 개발하는 것을 막고자 protected 접근지정자로 생성자 제한(= @NoArgsConstructor(access = AccessLevel.PROTECTED)*/

        //주문생성
        Order order = Order.createOrder(member, delivery, orderItem);
        /*Order order = new Order();로 개발하는 것을 막고자 protected 접근지정자로 생성자 제한(= @NoArgsConstructor(access = AccessLevel.PROTECTED)*/

        //주문 저장
        orderRepository.save(order);
        /*delivery를 deliveryRepository.save로 저장해야하고, orderItem 또한 orderItemRepository.save로 저장해야하는게 원칙*/
        /*하지만 order 도메인에서 cascade = CascadeType.ALL 로 지정해놓았기 때문에 order가 save 되는 순간 orderItem과 delivery도*/
        /*함께 save 된다. 이런 CascadeType.ALL의 경우는 private owner 가 한명인 경우에만 사용하는 것을 권장한다.*/

        return order.getId();

    }

    /*주문 취소*/
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문취소
        order.cancel();
        /*주문 취소와 관련되어 변경사항들은 더티체킹에 의해 쿼리가 날아간다.(jpa이점)*/
    }

    /*검색*/
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
