package jpabook;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Product extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "PRODUCT_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProductList;

    private int count;
    private int price;
    private LocalDateTime orderDateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
