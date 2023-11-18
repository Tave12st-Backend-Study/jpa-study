package jpashop.realspringjpa2.domain.service;

import javax.persistence.EntityManager;
import jpashop.realspringjpa2.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    void updateTest() throws Exception {
        // given
        Book book = em.find(Book.class, 1L);

        // TX
        book.setName("asdfasdf");

        // 변경감지 동작, dirty checking

    }

}
