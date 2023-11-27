package study.SpringDataJpa.repository.custom;

import java.util.List;
import study.SpringDataJpa.entity.Member;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
