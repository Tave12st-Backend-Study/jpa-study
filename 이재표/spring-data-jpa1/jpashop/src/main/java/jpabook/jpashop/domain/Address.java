package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String strret;
    private String zipcode;

    protected Address(){

    }
    public Address(String city, String strret, String zipcode) {
        this.city = city;
        this.strret = strret;
        this.zipcode = zipcode;
    }
}
