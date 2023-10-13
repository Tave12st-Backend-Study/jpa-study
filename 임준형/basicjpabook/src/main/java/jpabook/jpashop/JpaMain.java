package jpabook.jpashop;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("김영한");

            em.persist(book);

            System.out.println("----- 상속 관계일 때 엔티티 형태로 확인 -----");
            System.out.println("----- TYPE(m) = Member (상속 관계에서 사용) -----");
            System.out.println("----- 쿼리 나가는 시점 -----");
            List<Item> resultList = em.createQuery("select i from Item i where type(i) = Book", Item.class)
                    .getResultList();
            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("        select\n" +
                    "            item0_.ITEM_ID as ITEM_ID2_3_,\n" +
                    "            item0_.createBy as createBy3_3_,\n" +
                    "            .... ,\n" +
                    "            item0_.DTYPE as DTYPE1_3_ \n" +
                    "        from\n" +
                    "            Item item0_ \n" +
                    "        where\n" +
                    "            item0_.DTYPE='Book'");
            System.out.println("----- 실제 쿼리 끝 -----");

            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }
}
