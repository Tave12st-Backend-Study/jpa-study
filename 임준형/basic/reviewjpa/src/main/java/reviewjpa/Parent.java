package reviewjpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@Setter
public class Parent {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    및에 있는 childList를 모두 persist를 해줄것이다 = CascadeType.ALL
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        child.setParent(this);
        childList.add(child);
    }
}
