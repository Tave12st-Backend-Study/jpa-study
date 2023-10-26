package jpabook.jpashop01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Jpashop01Application {

    public static void main(String[] args) {
        Hello hello = new Hello();
        hello.setData("hello");
        String data = hello.getData();
        System.out.println("data = " + data);
        SpringApplication.run(Jpashop01Application.class, args);
    }

}
