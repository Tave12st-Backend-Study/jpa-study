package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    //readOnly = false를 지정 :: 리포지토리 저장하도록 함(메서드 단위 @Transactional이 우선권을 가지기 때문)
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId,String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);

        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        //==엔티티를 변경하는 방법 요약!==//
        //준영속상태 엔티티는 이렇게 id값과 데이터를 파라미터로 넘겨서
        //영속 상태에 있는 엔티티를 조회 -> 엔티티 값 변경 -> 트랜잭션 커밋 시점에 변경감지

        /*또한, 이렇게 set을 이용하기 보다, 엔티티에서 역추적할 수 있는 메서드를 생성해서 사용하는걸 권장
        * ex. findItem.change(name, price, stockQuantity);
        * */
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
