package hellojpa;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AddressEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Address address;
    public AddressEntity(String city,String street,String zipcode){
        this.address = new Address(city, street, zipcode);
    }
}
