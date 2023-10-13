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

            Team teamA = new Team();
            teamA.setName("teamA");

            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");

            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            member1.setAge(10);

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            member2.setAge(15);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);

            em.persist(member3);

            System.out.println("----- 업데이트 영향을 받은 엔티티 갯수를 return된다. -----");
            System.out.println("----- member의 3개의 객체가 영향을 받음 -----");
            int effectedCount = em.createQuery("update Member m set m.age = 20 where m.age < 20")
                    .executeUpdate();
            System.out.println("----- 한 번에 업데이트가 나감 -----");
            System.out.println("----- 영속성 컨텍스트에 바뀌지 않은 나이가 존재, 동시에 DB에 바뀐 내용이 존재 -----");


            System.out.println("---------- 영속성 컨텍스트 clear 전 ----------");
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("member1.getAge() = " + findMember.getAge()); // 10


            System.out.println("\n---------- 영속성 컨텍스트 clear 후 ----------");
            em.clear();
            Member afterCleanFindMember = em.find(Member.class, member1.getId());
            System.out.println("afterCleanFindMember.getAge() = " + afterCleanFindMember.getAge()); //20

            System.out.println("effectedCount = " + effectedCount); // 3


            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("상품을 update하거나 delete할 때 사용하는 [벌크연산]\n");

            System.out.println("재고가 10개 미만인 모든 상품의 가격을 10% 상승하려면?");
            System.out.println("JPA 변경 감지 기능으로 실행하려면 너무 많은 SQL이 실행된다.\n");

            System.out.println("1. 재고가 10개 미만인 상품을 리스트로 조회한다.");
            System.out.println("2. 상품 엔티티의 가격을 10% 증가한다.");
            System.out.println("3. 트랜잭션 커밋 시점에 변경 감지가 동작한다.");
            System.out.println("변경된 데이터가 100건이라면 100번의 UPDATE SQL이 실행되므로 비효율적이다.\n");

            System.out.println("이를 해결하는 것이 벌크연산이다.\n");

            System.out.println("벌크 연산은 영속성 컨텍스트를 무시하고 DB에 접근하는 쿼리이다.");
            System.out.println("그러므로, 벌크 연산을 먼저 실행하거나 벌크 연산 수행 후 영속성 컨텍스트를 초기화해야한다.");
            System.out.println("벌크연산을 하면 DB에는 바뀌었지만 애플리케이션에는 바뀌지 않았으므로 영속성 컨텍스트를 초기화해야한다.");
            System.out.println("어차피 쿼리가 나가면서 영속성 컨텍스트를 flush하기 때문에, 초기화만 해주면된다.");
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
