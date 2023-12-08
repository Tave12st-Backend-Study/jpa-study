
package study.querydsl.dto;

import lombok.Data;

@Data
public class UserDto
{
    private String name;
    private int age;

    public UserDto(String username, int age){
        this.name = username;
        this.age = age;
    }

    public UserDto() {
    }
}
