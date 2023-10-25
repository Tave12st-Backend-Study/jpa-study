package jpashop.realspringjpa1.domain.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpashop.realspringjpa1.domain.Order;
import jpashop.realspringjpa1.domain.OrderStatus;
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

//    public List<Order> findAll(OrderSearch orderSearch) {
//        orderSearch.getOrderStatus == OrderStatus.ORDER
//    }
}
