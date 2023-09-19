package jpa_basic_dionisos198;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MBR")
public class Member {
    @Id
    private Long id;
    @Column(nullable = false,length = 10)
    private String name;
    public Member(){

    }
    public Member(Long id,String name){
        this.id=id;
        this.name=name;
    }
    public Member(Long id){
        this.id=id;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }


}
