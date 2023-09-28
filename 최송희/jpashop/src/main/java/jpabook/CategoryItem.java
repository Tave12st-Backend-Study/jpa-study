package jpabook;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_ITEM")
//DB 중에 order가 안되는 이름이 있다.
public class CategoryItem {
    @Id
    @JoinColumn(name = "ITEM_ID")
    private Item id;


}
