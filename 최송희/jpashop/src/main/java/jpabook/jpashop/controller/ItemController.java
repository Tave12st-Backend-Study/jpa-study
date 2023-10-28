package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String itemList(Model model){
        List<Item> itemList = itemService.findItems();
        model.addAttribute("items", itemList);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);

        BookForm book = new BookForm();
        book.setId(itemId);
        book.setName(item.getName());
        book.setPrice(item.getPrice());
        book.setStockQuantity(item.getStockQuantity());
        book.setAuthor(item.getAuthor());
        book.setIsbn(item.getIsbn());

        model.addAttribute("form", book);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm bookForm) {

        /*Book book = new Book();
        book.setId(bookForm.getId());
        //jpa에 들어갔다가 나온 객체(식별자가 DB에 있으면 = 준영속 상태의 객체)
        //JPA가 식별할 수 있는 ID가 있기 때문, 더이상 jpa에서 관리하지 x 엔티티(준영속 엔티티)

        book.setName(bookForm.getName());
        book.setPrice(book.getPrice());
        book.setStockQuantity(book.getStockQuantity());
        book.setAuthor(book.getAuthor());
        book.setIsbn(book.getAuthor());

        itemService.saveItem(book);*/

        //위의 book 객체는 준영속 상태의 엔티티 이므로 JPA에서 관리 X
        //변경감지를 하지 못하므로

        //병합을 사용하지 말고 변경감지를 이용해 엔티티 수정하기
        itemService.updateItem(itemId, bookForm.getName(),bookForm.getPrice(), bookForm.getStockQuantity());
        return "redirect:/items";
    }


}
