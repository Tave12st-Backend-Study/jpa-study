package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!!!");
        //Model : 데이터 실어서 뷰로 넘길 수 있음
        //data 키에 값을 hello!!! 매핑
        return "hello";
        //hello.html이 자동으로 붙음 -> templates의 hello,html로 이동
    }
}
