package myproject.eumfruit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myproject.eumfruit.constant.ItemKind;
import myproject.eumfruit.constant.ItemSellStatus;
import myproject.eumfruit.dto.ItemFormDto;
import myproject.eumfruit.exception.OutOfStockException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    private String country; // 원산지

    @Enumerated(EnumType.STRING)
    private ItemKind itemKind; // 종류, 선물이냐 단품이냐

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private LocalDateTime regTime; // 등록 시간

    private LocalDateTime updateTime; // 수정 시간

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        // 주문완료시 재고를 감소시키는 로직
        int restStock = this.stockNumber - stockNumber; // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구한다.
        if(restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 : " + this.stockNumber+ ")");   // 예외발생
        }
        this.stockNumber = restStock;   // 재고 값 변경
    }

    public void addStock(int stockNumber) {
        // 주문취소시 재고 증가.
        this.stockNumber += stockNumber;
    }
}
