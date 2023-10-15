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

//    CascadeType.ALL, orphanRemoval = true 를 같이 사용하게 될 경우 부모 엔티티를 통해서 자식의 생명주기를 관리할 수 있음
//    == repository가 없어도 됨
//    도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        child.setParent(this);
        childList.add(child);
    }
}
