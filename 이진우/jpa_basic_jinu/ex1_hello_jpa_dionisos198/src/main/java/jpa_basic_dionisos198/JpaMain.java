package jpa_basic_dionisos198;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
            Member member=new Member();
            member.setName("hello");

            em.persist(member);

            em.flush();
            em.clear();
            Member refMember=em.getReference(Member.class,member.getId());
            Member findMember=em.find(Member.class,member.getId());
            System.out.println("refMember :"+refMember);
            System.out.println("findMember: "+findMember);

        }catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
