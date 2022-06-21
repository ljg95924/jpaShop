package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
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
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {

        // seter 보다 create 나 order의 static 생성자 메소드를 사용하는것이 제일 깔끔하고 조음
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
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";

    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
        /* merge를 하게되는 준영속 엔티티이다.
         준영속엔티티는 jpa가 관리하지않음.
         new 로 해서 만들었음
         이미 book은 jpa에 한번 들어갔다 나온 얘
         db에 자동적으로 수정되어 저장되지않음.
         준영속 엔티티를 수정하는 2가지방법
         1.변경감지기능 사용
         itemService의 updateItem() 부분 보기
         2. 병합(merge) 사용
         itemRepository_save()에서 찾아온 item의 값을 모두다 바꺼치기 해버림. 이 후 저장.
         자세한건 itemRepository_save()에
         코드 동작방식은 ipdateItem()의 소스랑 동일함.
         
        주의사항: 변경감지기능을 사용하면 원하는 속성만 선택하여 변경가능하지만
        merge는 모든 속성을 변경한다. 병합값이 없으면 null로 업데이트해버려서 위험.
         */
        /*// 병합방식
        Book book = new Book();
        book.setIsbn(form.getIsbn());
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);*/

        // 변경감지 방식
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
