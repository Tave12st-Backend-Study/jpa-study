package querydsl.querydsl;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.domain.Member;
import querydsl.querydsl.domain.QTestEntity;
import querydsl.querydsl.domain.Team;
import querydsl.querydsl.domain.TestEntity;

@SpringBootTest
@Transactional
public class QuerydslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		TestEntity testEntity = new TestEntity();
		em.persist(testEntity);

		JPAQueryFactory query = new JPAQueryFactory(em);
//		QTestEntity qTestEntity = new QTestEntity("testEntityt");
		QTestEntity qTestEntity = QTestEntity.testEntity;

		TestEntity result = query
				.selectFrom(qTestEntity)
				.fetchOne();

		assertThat(result).isEqualTo(testEntity);
		assertThat(result.getId()).isEqualTo(testEntity.getId());
	}

	public static Team generateTeam(String teamName) {
		return Team.builder()
				.name(teamName)
				.build();
	}

	public static Member generateMember(String userName, int age, Team team) {
		return Member.builder()
				.username(userName)
				.age(age)
				.team(team)
				.build();
	}

}
