package reviewjpa.superclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Setter @Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn
public class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
