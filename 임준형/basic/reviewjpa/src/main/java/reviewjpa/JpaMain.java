package reviewjpa;

import reviewjpa.superclass.Item;
import reviewjpa.superclass.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Member member = new Member();
            member.setUsername("proxy");

            em.persist(member);


            em.flush();
            em.clear();

            /* // em.find
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getUsername() = " + findMember.getUsername());
             */


            Member findMemberByReference = em.getReference(Member.class, member.getId());
            System.out.println("findMemberByReference.getClass() = " + findMemberByReference.getClass());
            System.out.println("findMemberByReference.getId() = " + findMemberByReference.getId());
            System.out.println("findMemberByReference.getUsername() = " + findMemberByReference.getUsername());

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
