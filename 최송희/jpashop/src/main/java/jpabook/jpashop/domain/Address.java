package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //jpa 스펙상 만들어 놓은 것 함부로 new 생성 방지
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
/*
* 값 타입 = 변경되서는 안된다. setter 제공x
* */