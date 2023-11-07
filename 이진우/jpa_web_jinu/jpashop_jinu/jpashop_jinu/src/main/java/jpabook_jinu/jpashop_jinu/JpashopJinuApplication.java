package jpabook_jinu.jpashop_jinu;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopJinuApplication {


	public static void main(String[] args) {

		Hello hello=new Hello();
		hello.setData("hello");
		String data=hello.getData();
		System.out.println("data = "+data);
		SpringApplication.run(JpashopJinuApplication.class, args);
	}
	@Bean
	Hibernate5Module hibernate5Module(){
		return new Hibernate5Module();
	}

}
