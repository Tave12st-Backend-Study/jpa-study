package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
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

    public List<Order> findAllWithMemberDeliver(int offset, int limit){
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        //이렇게 하면 order의 MEMBER, DELIVERY가 LAZY로 페치전략으로 되어있어도 한번에 실제 값으로 객체가 조회된다.
    }

    public List<Order> findAllWithItem(){
        return em.createQuery(
                "select distinct o from Order o"+
                " join fetch o.member m"+
                " join fetch o.delivery d"+
                " join fetch o.orderItems oi"+
                " join fetch oi.item i", Order.class)
                .getResultList();
        //distinct는 스프링에서 엔티티의 id값이 같은 데이터가 있으면 중복을 제거해준다(DB는 완전히 데이터가 같아야 중복 제거해주지만)
        //SQL 1번 실행(장점) + 페이징 불가(단점)
    }

}
