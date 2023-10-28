package jpabook_jinu.jpashop_jinu.domain.item;

import jpabook_jinu.jpashop_jinu.domain.Category;
import jpabook_jinu.jpashop_jinu.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DiscriminatorColumn(name = "dtype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories=new ArrayList<>();

    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    public void removeStock(int quantity){
        int restStock=stockQuantity-=quantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=restStock;
    }
}
