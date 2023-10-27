package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRepository {
    private final EntityManager em;

    @Transactional
    public void save(Item item){
        if(item.getId() == null){
            em.persist(item);
            // id값 == null로 확인하는 이유 : 최초 저장시에는 id값이 null이기 때문에 이것을 이용한 것
        }else em.merge(item); //업데이트의 의미
        /*변경감지와 병합 ***/
    }


    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
