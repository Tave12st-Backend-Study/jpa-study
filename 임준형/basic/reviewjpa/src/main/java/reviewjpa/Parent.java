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

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
//    및에 있는 childList를 모두 persist를 해줄것이다 = CascadeType.ALL
//    소유자가 하나일때만 사용가능, 다른 엔티티에서 몰라야함. 단일 엔티티에 종속적일때만 사용(orphanRemoval = true일때도 마찬가지)
//    orphanRemoval = true는 CascadeType.REMOVE와 비슷하게 동작함
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        child.setParent(this);
        childList.add(child);
    }
}
