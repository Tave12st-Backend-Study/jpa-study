package jpabook_jinu.jpashop_jinu.service;

import jpabook_jinu.jpashop_jinu.domain.*;
import jpabook_jinu.jpashop_jinu.domain.item.Item;
import jpabook_jinu.jpashop_jinu.repository.ItemRepository;
import jpabook_jinu.jpashop_jinu.repository.MemberRepositoryOld;
import jpabook_jinu.jpashop_jinu.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberRepositoryOld memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;


    @Transactional
    public Long order(Long memberId,Long itemId,int count){
        Member member=memberRepository.findOne(memberId);
        Item item=itemRepository.findOne(itemId);

        Delivery delivery=new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        OrderItem orderItem=OrderItem.createOrderItem(item,item.getPrice(),count);
        Order order= Order.createOrder(member,delivery,orderItem);

        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId){
        Order order=orderRepository.findOne(orderId);
        order.cancel();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
