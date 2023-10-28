package jpashop.realspringjpa1.domain.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpashop.realspringjpa1.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * generateValue, identity 일 경우 id가 null인 상태에서
     * em.persist할 때 생성되어 저장되기 때문에, 당연히 persist가 될 것이다.
     */

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
