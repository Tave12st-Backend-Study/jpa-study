package jpa_book_dionisos198.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    @Lob
    private String name;
    private int price;
    private int stockQuantity;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    @ManyToMany(mappedBy = "items")
    private List<Category> categories=new ArrayList<>();
    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Item() {
    }
}
