package hellojpa.jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter @Setter
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zip;
}
