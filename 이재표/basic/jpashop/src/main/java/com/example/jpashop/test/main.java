package com.example.jpashop.test;

import com.example.jpashop.Book;
import com.example.jpashop.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static javax.persistence.Persistence.createEntityManagerFactory;

public class main {
    public static void main(String[] args) {
        EntityManagerFactory emf = createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Team teamA=new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB=new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            for(int i=0;i<50;i++){
                Member member=new Member();
                member.setUsername("회원"+(i+1));
                member.setTeam(teamA);
                em.persist(member);
            }
            for(int i=0;i<50;i++){
                Member member=new Member();
                member.setUsername("회원"+(i+51));
                member.setTeam(teamB);
                em.persist(member);
            }

            em.flush();
            em.clear();
            System.out.println("======================");
            List<Team> teams=em.createQuery("select t from Team t", Team.class).getResultList();
            for (Team team : teams) {
                System.out.println("team:"+team.getName());
                for(Member mem:team.getMembers()){
                    System.out.println(mem.getUsername());
                }
            }


            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
