package study.SpringDataJpa.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.SpringDataJpa.entity.Item;

@SpringBootTest
class ItemRepositoryTest {9

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() {
        Item item = new Item("A");
        itemRepository.save(item);
    }
}