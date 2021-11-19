package myproject.eumfruit.Repository;

import myproject.eumfruit.constant.ItemKind;
import myproject.eumfruit.dto.ItemSearchDto;
import myproject.eumfruit.dto.ProductItemDto;
import myproject.eumfruit.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    // Querydls을 Spring Data Jpa와 함께 사용하기 위해 사용자 정의 리포지토리를 정의해야 한다.
    /*
    1. 사용자 정의 인터페이스 작성
    2. 사용자 정의 인터페이스 구현
    3. Spring Data Jpa 리포지토리에서 사용자 정의 인터페이스 상속
     */
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    /* 상품 조회 조건을 담고 있는 itemSearchDto 객체와 페이징 정보를 담고 있는 pageable 객체를 파라미터로 받는
    getAdminItemPage 메소드를 정의한다. 반환 데이터로 Page<item> 객체를 반환한다.
     */
    List<ProductItemDto> getProductItemList(ItemKind itemKind);
}
