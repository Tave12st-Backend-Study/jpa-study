package jpa_jpql_dionisos198;

public class MemberDTO {
    private String username;
    private int age;

    public String getUsername() {
        return username;
    }

    public MemberDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
