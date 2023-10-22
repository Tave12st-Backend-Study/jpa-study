package jpashop.realspringjpa1.domain;

import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
