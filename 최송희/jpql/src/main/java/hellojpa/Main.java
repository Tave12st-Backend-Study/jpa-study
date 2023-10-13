package hellojpa;


import javax.persistence.*;
import java.util.Collection;
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

                    Member member = new Member();
                    member.setUsername("관리자");
                    member.setAge(10);
                    member.setMemberType(MemberType.ADMIN);

                    member.changeTeam(team);
                    em.persist(member);

                    Member member2 = new Member();
                    member.setUsername("관리자2");
                    member.setAge(20);
                    member.setMemberType(MemberType.ADMIN);

                    member.changeTeam(team);
                    em.persist(member2);

                    em.flush();
                    em.clear();

                    String qeuryAn = "select m.team from Member m";
                    //묵시적 내부 조인(단일 값 연관경로)

                    String query = "select t.members from Team t";
                    //묵시적 내부 조인(컬렉션 값 연관경로)

                    //컬렉션 값 연관경로 .. 탐색불가..컬렉션 자체가 반환되기에
                    //대안 : from절을 통한 명시적 조인을 통해 가능

                    String queryDv = "select m.username from Team t join t.members m";
                    //명시적 조인 (컬렉션 값 연관경로) -join키워드 직접 사용

                    //별칭을 통해 탐색 가능

                    //결론: 묵시적 조인 쓰지 않는다. 쿼리 튜닝하기도 어렵다. 명시적 조인 추천

                    List resultList = em.createQuery(query, Collection.class).getResultList();

                    for(Object s : resultList){
                        System.out.println(s);
                    }


                }catch(Exception e){
                    tx.rollback();
                    e.printStackTrace();
                }finally {
                    em.close();
                }
                emf.close();

    }
}