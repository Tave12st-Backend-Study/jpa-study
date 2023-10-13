package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

//            em.flush();
//            em.clear();

            // Order 안에 있는 임베디드 Adress 꺼내기가능, order에 의존적, 이것이 임베디드 타입의 한계
            List<Order> resultList = em.createQuery("select o.address from Order o", Order.class)
                    .getResultList();

//            System.out.println("----- 쿼리 나가는 시점 -----");
//            System.out.println("----- 쿼리 끝나는 시점 -----");

//            System.out.println("----- 실제 쿼리 -----");
//            System.out.println("----- 실제 쿼리 끝 -----");

            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
            e.printStackTrace();
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }

}
