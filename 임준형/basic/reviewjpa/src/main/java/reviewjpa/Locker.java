//package reviewjpa;
//
//import lombok.Getter;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//
//@Getter
//@Entity
//public class Locker {
//    // 1대 1 매핑 예제
//
//    @Id @GeneratedValue
//    private Long id;
//
//    private String name;
//
//    @OneToOne(mappedBy = "locker")
//    private Member member;
//}
