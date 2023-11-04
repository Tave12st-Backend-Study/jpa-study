package jpabook.jpashop.repository.order.query;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;
    //화면과 밀접한 레포지토리는 관심사 분리를 통해 작성할 수 있어야 한다

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders(); //query 1번 -> n개

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    public List<OrderItemQueryDto> findOrderItems(Long orderId){
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId",
                        OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }


    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
                        +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }

}
