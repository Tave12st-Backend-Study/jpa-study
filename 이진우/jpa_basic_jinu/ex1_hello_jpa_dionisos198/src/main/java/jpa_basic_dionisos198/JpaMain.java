package jpa_basic_dionisos198;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
            String jpql="select m from Member m where m.name like '%kim%'";
            List<Member> result=em.createQuery(jpql,Member.class).getResultList();

           /* for (Member member : result) {
                System.out.println(member.getName());
            }*/

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
