package reviewjpa.superclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Setter
@Getter
@DiscriminatorValue(value = "A")
public class Album extends Item{

    private String artist;
}
