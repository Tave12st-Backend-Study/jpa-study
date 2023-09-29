package reviewjpa.superclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Setter @Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
