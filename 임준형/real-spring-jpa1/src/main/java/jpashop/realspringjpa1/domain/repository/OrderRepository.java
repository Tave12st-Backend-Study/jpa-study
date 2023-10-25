package jpashop.realspringjpa1.domain.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpashop.realspringjpa1.domain.Order;
import jpashop.realspringjpa1.domain.OrderStatus;
import jpashop.realspringjpa1.domain.repository.dto.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


    /**
     *
     * 파라미터 값이 만약에 비어 있다면? -> queryDSL로 해결
     *
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        return em.createQuery("select o From Order o join o.member m"
                        + " where o.status = :status"
                        + " and m.username like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000)
                .getResultList();
    }
}
