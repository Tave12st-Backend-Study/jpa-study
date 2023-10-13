package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();


            System.out.println("----- 경토 표현식 중 상태필드 -----");
            System.out.println("----- 경로 탐색의 끝, 탐색 X -----");
            // 상태 필드
            String statusFieldQuery = "select m.username From Member m ";


            System.out.println("----- 단일 값 연관 경로: 묵시적 내부 조인(inner join) 발생 !!! DB에서 자동 조인 - 매우 중요 -----");
            System.out.println("----- xxxToOne, 추가 탐색 가능 -----");
            // 단일 값 연관 경로
            String singleValueRelationPath = "select m.team From Member m ";


            System.out.println("----- 컬렉션 값 연관 경로: 묵시적 내부 조인(inner join) 발생 !!! DB에서 자동 조인 - 매우 중요 -----");
            System.out.println("----- xxxToMany, 컬렉션에서는 추가 탐색 불가 -----");
            // 컬렉션 값 연관 경로
            String collectionValueRelationPath = "select t.memberList From Team t ";
            // ex) team의 Member를 조인해서 username을 찍고 싶을 때 탐색이 불가능함 명시적인 조인을 써야함
            String collectionValueRelationPath2 = "select m.username From Team t join t.memberList m "; // 명시적조인

            List<String> result1 = em.createQuery(statusFieldQuery, String.class).getResultList();
            List<Team> result2 = em.createQuery(singleValueRelationPath, Team.class).getResultList();
            Collection result3 = em.createQuery(collectionValueRelationPath, Collection.class).getResultList();
            List<String> result4 = em.createQuery(collectionValueRelationPath2, String.class).getResultList();

            for (String s : result4) {
                System.out.println("username = " + s);
            }

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("묵시적 내부 조인 절대 금지!! 명시적인 조인을 사용할 것");
            System.out.println("묵시적 조인시 항상 내부 조인이다.");
            System.out.println("컬렉션은 경로 탐색의 끝이며, 명시적 조인을 통해 별칭을 꼭 얻어야 사용 가능하다.");
            System.out.println("경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM (JOIN) 절에 영향을 준다");
            System.out.println("-------------------------------------------------------------------------------------");

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
