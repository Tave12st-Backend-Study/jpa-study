package jpashop.realspringjpa2.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import jpashop.realspringjpa2.domain.Order;
import jpashop.realspringjpa2.repository.dto.OrderSearch;
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
     * 파라미터 값이 만약에 비어 있다면? -> queryDSL로 해결
     */
    public List<Order> findAll(OrderSearch orderSearch) {
        return em.createQuery("select o From Order o join o.member m"
                        + " where o.status = :status"
                        + " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getName())
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
        if (StringUtils.hasText(orderSearch.getName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getName())) {
            query = query.setParameter("name", orderSearch.getName());
        }

        return query.getResultList();
    }

    // 엔티티로 조회
    public List<Order> findAllWithMemberDelivery() {

        /**
         *  Lazy 인 것들만 전부 한번에 조회
         *  fetch join으로 order -> member, order -> delivery는 이미 조회 된 상태이므로 지연로딩이 아님
         */
        return em.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class
        ).getResultList();
    }
    /**
     * distinct 덕분에 같은 order 엔티티가 겹칠 경우 이를 제거해준다.
     * 컬렉션 fetch join 하면 페이징이 불가능함.
     * 어차피 다 갖고와서 메모리에서 페이징처리하는 것이니 페이징 처리가 불가능
     */
}
