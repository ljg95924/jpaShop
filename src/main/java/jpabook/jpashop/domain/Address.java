package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable // 내장 타입
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
