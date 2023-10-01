package jpa_basic_dionisos198;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Member extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT")
    private List<Product> products=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts=new ArrayList<>();




    public Member(){

    }



    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
   @Embedded
    private Period workPeriod;

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="city",column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name="street",column = @Column(name ="WORK_STREET")),
            @AttributeOverride(name="zipcode",column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address homeAddress;

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }

    @Embedded
    private Address workAddress;


}
