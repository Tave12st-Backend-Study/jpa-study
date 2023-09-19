package jpa_basic_dionisos198;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
           Member member=new Member(373737L,"jinujinu");
           em.persist(member);
           System.out.println("-----------------------------1---------------------------");
            List<Member> selectMFromMemberM = em.createQuery("select m from Member m").getResultList();
           System.out.println("-----------------------------2----------------------------");
            em.detach(member);
            member.setName("kkkk");
            System.out.println("----------------------------3----------------------------");
            tx.commit();
            System.out.println("----------------------------4-----------------------------");

        }catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
