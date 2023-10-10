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
           Team team=new Team();
           team.setName("teamA");
           em.persist(team);

           Member member=new Member();
           member.setUsername("member");
           member.setAge(10);
           member.changeTeam(team);
           member.setType(MemberType.ADMIN);
            em.persist(member);
            em.flush();
            em.clear();

            String query="select case m.username when 'member' then 'hi' else 'Bye' end from Member m";
            List<String> list=em.createQuery(query,String.class).getResultList();
            System.out.println(list);
            String query2="select coalesce(m.username,'이름없는 회원') from Member m";
            List<String> list2=em.createQuery(query2,String.class).getResultList();
            System.out.println(list2);
            String query3="select NULLIF(m.username,'관리자') from Member m";
            List<String> list3=em.createQuery(query3,String.class).getResultList();
            System.out.println(list3);
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
