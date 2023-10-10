package jpa_jpql_dionisos198;

import javax.persistence.*;
import java.lang.management.MemoryManagerMXBean;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
           Member member1=new Member();
           member1.setUsername("관리자1");
           em.persist(member1);

           Member member2=new Member();
           member2.setUsername("관리자2");
           em.persist(member2);
           em.flush();
           em.clear();
         String query="select function('group_concat',m.username) From Member m";
         List<String> result=em.createQuery(query,String.class).getResultList();
            for (String s : result) {
                System.out.println("s = "+s);
            }
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
