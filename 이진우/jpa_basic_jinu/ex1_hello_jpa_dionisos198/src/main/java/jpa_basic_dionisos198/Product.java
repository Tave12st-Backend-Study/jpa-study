package jpa_basic_dionisos198;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;

    private String name;

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
    @ManyToMany(mappedBy = "products")
    private List<Member> members=new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts=new ArrayList<>();

}
