package jpabook;

import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Member member1 = new Member();
            member1.setUserName("hello");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUserName("hello");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1Reference = em.getReference(Member.class, member1.getId());
            System.out.println("m1 = :"+m1Reference.getClass());//Proxy


            Member m2 = em.getReference(Member.class, member1.getId());
            System.out.println("m2 = :"+m2.getClass());//Proxy

            System.out.println("m1Referece == m2 : "+ (m1Reference == m2));


            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
    private static void logic(Member m1, Member m2){
        System.out.println("m1 :"+(m1 instanceof Member));
        System.out.println("m1 :"+(m2 instanceof Member));

    }
}