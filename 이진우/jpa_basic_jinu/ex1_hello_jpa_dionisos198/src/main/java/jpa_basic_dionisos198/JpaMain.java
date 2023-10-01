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
            Address address=new Address("city","street","10000");
            Member member=new Member();
            member.setName("member1");
            member.setHomeAddress(address);
            em.persist(member);

            Member member2=new Member();
            member2.setName("member2");
            member2.setHomeAddress(address);
            em.persist(member2);

            member.getHomeAddress().setCity("newCity");
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
