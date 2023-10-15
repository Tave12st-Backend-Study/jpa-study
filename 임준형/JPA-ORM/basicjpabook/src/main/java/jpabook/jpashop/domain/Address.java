package jpabook.jpashop.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
//@EqualsAndHashCode
public class Address {

    @Column(length = 10)
    private String city;
    @Column(length = 15)
    private String street;
    @Column(length = 20)
    private String zipcode;

    public String fullAddress() {
        return getCity() + getStreet() + getZipcode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
