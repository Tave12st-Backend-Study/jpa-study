package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
    public List<Member> findByUsernameAndAgeGreaterThan(String username,int age);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);


    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id,m.username,t.name) "+ "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    List<Member> findListByUsername(String name);

    Member findMemberByUsername(String name);

    Optional<Member> findOptionalByUsername(String username);


    Page<Member> findPageByUsername(String name, Pageable pageable);

    Slice<Member> findSliceByUsername(String name,Pageable pageable);

    List<Member> findListByUsername(String name,Pageable pageable);

    List<Member> findByUsername(String name, Sort sort);

    Page<Member> findByAge(int age,Pageable pageable);

    Slice<Member> findSliceByAge(int age,Pageable pageable);

    @Query(value = "select m from Member m left join m.team t",countQuery = "select count(m) from Member m")
    Page<Member> findJoinByAge(int age,Pageable pageable);


    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left  join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @QueryHints(value = @QueryHint(name="org.hibernate.readOnly",value = "true"))
    Member findReadOnlyByUsername(String username);

    @Query(value = "select age from member where username =?",nativeQuery = true)
    int findByNativeQuery(String username);



}
