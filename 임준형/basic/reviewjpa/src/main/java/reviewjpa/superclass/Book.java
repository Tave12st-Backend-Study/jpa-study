package reviewjpa.superclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Setter @Getter
@DiscriminatorValue(value = "B")
public class Book extends Item{

    private String author;
    private String isbn;
}
