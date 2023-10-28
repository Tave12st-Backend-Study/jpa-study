package jpabook.jpashop.domain.Item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /*
    비즈니스 로직
    */

    public void addStockQuantity(int stockQuantity){
        this.stockQuantity += stockQuantity;
    }

    //재고수량 감소
    public void removeStockQuantity(int stockQuantity){

        if(stockQuantity > this.stockQuantity){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity -= stockQuantity;
    }
}
