package study.SpringDataJpa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.SpringDataJpa.entity.Member;
import study.SpringDataJpa.repository.dto.MemberDto;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.SpringDataJpa.repository.dto.MemberDto(m.id, m.username, t.name) "
            + "from Member m join m.team t")
    List<MemberDto> findMemberDto();

}
