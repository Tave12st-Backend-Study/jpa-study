package study.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.SpringDataJpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
