package myproject.eumfruit.Controller;

import lombok.RequiredArgsConstructor;
import myproject.eumfruit.Repository.ItemRepository;
import myproject.eumfruit.dto.ItemFormDto;
import myproject.eumfruit.dto.ItemSearchDto;
import myproject.eumfruit.entity.Item;
import myproject.eumfruit.service.ItemService;
import org.dom4j.rule.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value="/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if(bindingResult.hasErrors()) {
            return "item/itemForm"; //  상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환한다.
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {   // 상품 등록 시 첫번째 이미지가 없으면 에러메시지와 함께 상품등록 페이지로 전환
            // 상품의 첫번째 이미지는 메인 페이지에서 보여줄 상품 이미지로 사용하기 위해 필수 값으로 지정한다.
            model.addAttribute("errorMessage", "첫번째 상품이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList); // 상품 저장 로직을 호출, 매개변수로 상품정보와 상품 이미지 정보를 담고 있는 itemImgFileList를 넘겨준다.
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";    // 상품이 정상적으로 등록되었다면 메인 페이지로 이동한다.
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);   // 조회한 상품 데이터를 모델에 담아서 뷰로 전달.
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);   // 상품 수정 로직을 호출
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    //@GetMapping(value = {"/admin/items", "/admin/items/{page}"})    // value에 상품 관리 화면 진입 시 url에 페이지 번호가 없는 경우와 있는 경우 2가지를 매핑한다.
    @GetMapping(value = "/admin/items")
    public String itemManage(ItemSearchDto itemSearchDto, Model model) {
        //Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        /* 페이징을 위해 PageRequest.of 메소드를 통해 Pageable 객체 생성하고 파라미터로 조회할 페이지 번호, 한번에 가지고올 데이터의 수를 넣는다.
        Url경로에 페이지 번호가 있으면 해당페이지를 조회하도록 세팅하고, 페이지 번호가 없으면 0페이지를 조회하도록 한다.
         */

//        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);   // 조회 조건과 페이징 정보를 파라미터로 넘겨서 Page<Item> 객체를 반환 받는다.
//        System.out.println("조회한 데이터 : "+items.getContent()+", "+items.getTotalElements());
        List<Item> items = itemService.findAll();
        for(int i = 0; i<items.size(); i++) {
            System.out.println("데이터 : "+items.get(i));
        }

        model.addAttribute("items", items); // 조회한 상품 데이터 및 페이징 정보를 뷰에 전달한다.
        //model.addAttribute("itemSearchDto", itemSearchDto); // 페이지 전환 시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달한다.
        //model.addAttribute("maxPage", 5);   // 상품 관리 메뉴 하단에 보여줄 페이지 번호의 최대 개수이다. 5로 설정했으므로 최대 5개의 이동할 페이지 번호만 보여준다.
        return "item/itemMng";
    }
}
