package jpashop.realspringjpa1.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import jpashop.realspringjpa1.domain.Address;
import jpashop.realspringjpa1.domain.Member;
import jpashop.realspringjpa1.domain.Order;
import jpashop.realspringjpa1.domain.OrderStatus;
import jpashop.realspringjpa1.domain.exception.NotEnoughStockException;
import jpashop.realspringjpa1.domain.item.Book;
import jpashop.realspringjpa1.domain.item.Item;
import jpashop.realspringjpa1.domain.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void 상품_주문() throws Exception {
        //given
        Member member = createMember();

        Item book = createBook("TAVE 12기 준형", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        System.out.println("_________________________________________________++++++++++++++++++++++");
        System.out.println(getOrder);
        System.out.println("_________________________________________________++++++++++++++++++++++");


        assertThat(getOrder.getStatus())
                .as("상품 주문시 상태는 ORDER")
                .isEqualTo(OrderStatus.ORDER);

        assertThat(getOrder.getOrderItems().size())
                .as("주문한 상품 종류 수가 정확해야 한다.")
                .isEqualTo(1);

        assertThat(getOrder.getTotalPrice())
                .as("주문 가격은 가격 * 수량이다.")
                .isEqualTo(10000 * orderCount);

        assertThat(book.getStockQuantity())
                .as("주문 수량만큼 재고가 줄어야한다.")
                .isEqualTo(8);
    }

    @Test
    void 상품주문_재고수량초과() throws Exception {
        //given
        assertThrows(NotEnoughStockException.class, () -> {
            Member member = createMember();
            Item book = createBook("TAVE 12기 준형", 10000, 10);

            int orderCount = 11;
            orderService.order(member.getId(), book.getId(), orderCount);
        });
        //when

        //then
    }

    @Test
    void 주문_취소() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("TAVE 12기", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertThat(findOrder.getStatus())
                .isEqualTo(OrderStatus.CANCEL);

        assertThat(book.getStockQuantity())
                .isEqualTo(10);
    }

    private Member createMember() {
        Member member = new Member();
        member.setUsername("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}