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

            em.flush();
            em.clear();

            Member m1Reference = em.getReference(Member.class, member1.getId());
            System.out.println("m1 = :"+m1Reference.getClass());//Proxy

//            em.detach(m1Reference);//영속성 컨텍스트에서 관리 안해!
            em.close();

            m1Reference.getUserName();//SELECT 쿼리 발생(영속성 컨텍스트의 도움을 받아서)


            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
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