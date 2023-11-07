package jpabook_jinu.jpashop_jinu.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("A")
@Getter
@Setter
public class Album extends Item{
    private String artiest;
    private String etc;

}
