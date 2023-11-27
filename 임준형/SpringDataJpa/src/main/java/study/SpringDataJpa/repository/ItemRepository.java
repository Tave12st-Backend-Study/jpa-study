package study.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.SpringDataJpa.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
