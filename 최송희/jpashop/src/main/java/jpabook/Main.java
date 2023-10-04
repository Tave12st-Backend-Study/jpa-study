package jpabook;

import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUserName("hello");
            member1.setTeam(team);
            em.persist(member1);

            em.flush();
            em.clear();

            //프록시 = FetchType.LAZY
            Member m = em.find(Member.class, member1.getId());//member만 SELECT
            System.out.println("team = "+m.getTeam().getClass());//Team 정보는 프록시로 가져온 것


            System.out.println("==================");
            System.out.println("team.getName() = "+m.getTeam().getName());
            System.out.println("==================");

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