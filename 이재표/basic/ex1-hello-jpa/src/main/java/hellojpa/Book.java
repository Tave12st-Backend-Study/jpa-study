package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter @Setter
@Entity
public class Book extends Item{
    @Id
    @GeneratedValue
    private Long id;
    private String isbn;
    private String title;
    @ManyToOne
    private BookStore bookStore;
}
