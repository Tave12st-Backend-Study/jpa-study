package jpabook_jinu.jpashop_jinu.web;

import jpabook_jinu.jpashop_jinu.domain.Member;
import jpabook_jinu.jpashop_jinu.domain.OrderStatus;
import jpabook_jinu.jpashop_jinu.domain.item.Item;
import jpabook_jinu.jpashop_jinu.service.ItemService;
import jpabook_jinu.jpashop_jinu.service.MemberService;
import jpabook_jinu.jpashop_jinu.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value = "/order")
    public String createForm(Model model){
        List<Member> members=memberService.findMembers();
        List<Item> items=itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }
    @PostMapping(value = "/order")
    public String order(@RequestParam("memberId") Long memberId,@RequestParam("itemId") Long itemId,@RequestParam("count") int count){
        orderService.order(memberId,itemId,count);
        return "redirect:/orders";
    }
}
