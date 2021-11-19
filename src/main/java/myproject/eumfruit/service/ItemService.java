package myproject.eumfruit.service;

import lombok.RequiredArgsConstructor;
import myproject.eumfruit.Repository.ItemImgRepository;
import myproject.eumfruit.Repository.ItemRepository;
import myproject.eumfruit.constant.ItemKind;
import myproject.eumfruit.dto.ItemFormDto;
import myproject.eumfruit.dto.ItemImgDto;
import myproject.eumfruit.dto.ItemSearchDto;
import myproject.eumfruit.dto.ProductItemDto;
import myproject.eumfruit.entity.Item;
import myproject.eumfruit.entity.Itemimg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final ItemRepository itemRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품등록
        Item item = itemFormDto.createItem();   // 상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성한다.
        itemRepository.save(item);  // 상품 데이터를 저장한다.

        // 이미지 등록
        for(int i =0; i<itemImgFileList.size(); i++) {
            Itemimg itemImg = new Itemimg();
            itemImg.setItem(item);
            if(i == 0)  // 첫번째 이미지는 대표이미지로 설정하고
                itemImg.setRepimgYn("Y");
            else    // 나머지 이미지는 N으로 설정한다.
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));    // 상품의 이미지 정보를 저장한다.
        }

        return item.getId();
    }

    @Transactional(readOnly = true) // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정
    public ItemFormDto getItemDtl(Long itemId) {
        List<Itemimg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품의 이미지를 조회한다.
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(Itemimg itemImg : itemImgList) {        // 조회한 itemimg 엔티티를 itemImgDto 객체로 만들어서 리스트에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)  // 상품의 아이디를 통해 상품 엔티티를 조회, 종재하지 않을 때는 예외발생
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new); // 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회
        item.updateItem(itemFormDto);   // 상품 등록 화면으로부터 전달 받은 itemFormDto를 통해 상품 엔티티를 업데이트한다.
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));    // 상품 이미지를 업데이트하기 위해 updateItemImg() 메소드에 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 넘긴다.
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        System.out.println("쿼리해올게요!!");
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    // 상품의 종류로 상품목록 가져오기
    @Transactional(readOnly = true)
    public List<Item> getItemByKind(ItemKind itemKind) {
        return itemRepository.findByItemKind(itemKind);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<ProductItemDto> getProductItemList(ItemKind itemKind){
        return itemRepository.getProductItemList(itemKind);
    }
}
