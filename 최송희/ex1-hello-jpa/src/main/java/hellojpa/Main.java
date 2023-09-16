package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // "hello"는 Persistence Unit의 이름과 일치해야 합니다.

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin(); //트랜잭션 시작

        //code 작성
        Member member = new Member();
        member.setId(1L);
        member.setName("helloA");

        em.persist(member);

        tx.commit();

        em.close();

        emf.close();


    }
}