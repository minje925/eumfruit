package myproject.eumfruit.service;

import lombok.RequiredArgsConstructor;
import myproject.eumfruit.Repository.ItemImgRepository;
import myproject.eumfruit.Repository.ItemRepository;
import myproject.eumfruit.dto.ItemFormDto;
import myproject.eumfruit.entity.Item;
import myproject.eumfruit.entity.Itemimg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
}
