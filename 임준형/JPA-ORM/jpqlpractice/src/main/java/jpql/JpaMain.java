package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Team teamA=new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB=new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Team teamC=new Team();
            teamC.setName("팀C");
            em.persist(teamC);

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

            for(int i=0;i<50;i++){
                Member member=new Member();
                member.setUsername("회원"+(i+101));
                member.setTeam(teamC);
                em.persist(member);
            }

            em.flush();
            em.clear();
            System.out.println("======================");
            List<Team> teams=em.createQuery("select t from Team t", Team.class).getResultList();
            for (Team team : teams) {
                System.out.println("team:"+team.getName());
                for(Member mem:team.getMemberList()){
                    System.out.println(mem.getUsername());
                }
            }
            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
            e.printStackTrace();
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }

}
