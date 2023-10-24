package jpabook.jpashop.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UpdateItemDto {
     private String name;
     private int price;
     private int stockQuantity;
     private String author;
     private String isbn;
}
