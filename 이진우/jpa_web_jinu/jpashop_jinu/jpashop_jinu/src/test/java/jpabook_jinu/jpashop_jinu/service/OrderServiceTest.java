package jpabook_jinu.jpashop_jinu.service;


import jpabook_jinu.jpashop_jinu.domain.Address;
import jpabook_jinu.jpashop_jinu.domain.Member;
import jpabook_jinu.jpashop_jinu.domain.Order;
import jpabook_jinu.jpashop_jinu.domain.OrderStatus;
import jpabook_jinu.jpashop_jinu.domain.item.Book;
import jpabook_jinu.jpashop_jinu.domain.item.Item;
import jpabook_jinu.jpashop_jinu.exception.NotEnoughStockException;
import jpabook_jinu.jpashop_jinu.repository.MemberRepository;
import jpabook_jinu.jpashop_jinu.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member=createMember();
        Item item=createBook("시골 JPA",10000,10);
        int orderCount=2;

        //when
        Long orderId= orderService.order(member.getId(),item.getId(),orderCount);
        //then
        Order getOrder=orderRepository.findOne(orderId);

        org.junit.jupiter.api.Assertions.assertEquals(OrderStatus.ORDER,getOrder.getOrderStatus(),"상품 주문시 상태는 ORDER");
        org.junit.jupiter.api.Assertions.assertEquals(1,getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 한다");
        org.junit.jupiter.api.Assertions.assertEquals(10000*2,getOrder.getTotalPrice(),"주문 가격은 가격* 수량이다");
        org.junit.jupiter.api.Assertions.assertEquals(8,item.getStockQuantity(),"주문 수량 만큼 재고가 줄어야 한다");

    }
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member=createMember();
        Item item=createBook("시골 JPA",10000,10);
        int orderCount=11;

        //when
        NotEnoughStockException thrown = assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(),orderCount));
        //then
        assertEquals("need more stock", thrown.getMessage());

    }

    @Test
    public void 주문취소(){
        //given
        Member member=createMember();
        Item item=createBook("시골 JPA",10000,10);
        int orderCount=2;
        Long orderId= orderService.order(member.getId(),item.getId(),orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder=orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL,getOrder.getOrderStatus(),"주문 최소시 상태는 CANCEL 이다");
        assertEquals(10,item.getStockQuantity(),"주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }



    private Member createMember(){
        Member member=new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity){
        Book book=new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}