package jpashop.realspringjpa1.domain.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import jpashop.realspringjpa1.domain.Order;
import jpashop.realspringjpa1.domain.OrderStatus;
import jpashop.realspringjpa1.domain.repository.dto.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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
                .setParameter("name", orderSearch.getUsername())
                .setMaxResults(1000)
                .getResultList();
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getUsername())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.username like :username";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getUsername())) {
            query = query.setParameter("username", orderSearch.getUsername());
        }

        return query.getResultList();
    }
}
