package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;
    //저장
    public void save(Order order){
        em.persist(order);
    }

    //한 건 조회
    public Order findOne(Long orderId){
        return em.find(Order.class, orderId);
    }

    //검색 조회
    public List<Order> findAllByString(OrderSearch orderSearch){
        return em.createQuery("select o from Order o join o.member m", Order.class)
                .getResultList();
    }
}
