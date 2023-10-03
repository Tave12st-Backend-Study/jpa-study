package reviewjpa;

import lombok.*;

import javax.persistence.Embeddable;

@Setter
@Getter

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    // 주소
    private String city;
    private String street;
    private String zipcode;
}
