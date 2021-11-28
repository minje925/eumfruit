package myproject.eumfruit.Repository;

import myproject.eumfruit.entity.Itemimg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<Itemimg, Long> {
    List<Itemimg> findByItemIdOrderByIdAsc(Long itemId);    // 상품 이미지 아이디의 오름차순으로 가져오는 쿼리 메소드

    Itemimg findByItemIdAndRepimgYn(Long itemId, String repimgYn);  // 상품의 대표이미지를 찾는 쿼리 메소드
}
