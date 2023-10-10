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
            Member member=new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            //List<Team> result=em.createQuery("select m.team from Member m",Team.class).getResultList();
            List<Team> result=em.createQuery("select t from Member m join m.team t").getResultList();
            em.createQuery("select o.address from Order o",Address.class).getResultList();
            //em.createQuery("select distinct m.username, m.age from Member m").getResultList();
            List<MemberDTO> resultList=em.createQuery("select new jpa_jpql_dionisos198.MemberDTO(m.username,m.age) from Member m", MemberDTO.class).getResultList();
            MemberDTO memberDTO=resultList.get(0);
            System.out.println("memberDTO = "+memberDTO.getUsername());
            System.out.println("memberDTO = "+memberDTO.getAge());

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
