package reviewjpa;

import lombok.*;

import javax.persistence.Column;
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

    // 컬럼명 재정의 가능
//    @Column(name = "ZIPCODE")
    private String zipcode;
}
