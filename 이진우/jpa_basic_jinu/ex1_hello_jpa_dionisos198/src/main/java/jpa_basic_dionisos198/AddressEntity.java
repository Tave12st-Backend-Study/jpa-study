package jpa_basic_dionisos198;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AddressEntity {
    @Id @GeneratedValue
    private Long id;

    private Address address;

    public Long getId() {
        return id;
    }

    public AddressEntity(String city,String street,String zipcode) {
        this.address=new Address(city,street,zipcode);
    }

    public AddressEntity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
