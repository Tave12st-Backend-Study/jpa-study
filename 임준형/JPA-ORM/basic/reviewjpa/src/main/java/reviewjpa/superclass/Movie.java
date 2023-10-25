package reviewjpa.superclass;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Setter @Getter
//@DiscriminatorValue(value = "M")
public class Movie extends Item{
    private String director;
    private String actor;
}
