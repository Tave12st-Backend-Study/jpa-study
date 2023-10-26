package jpabook_jinu.jpashop_jinu;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    private Long id;

    private String username;
}
