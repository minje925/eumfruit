package myproject.eumfruit.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연로딩으로 변경, 이걸하지 않으면 연관된 테이블을 모두 쿼리하는 비효율성이 생김
    @JoinColumn(name = "item_id")
    private Item item;      // 1개의 상품은 여러 주문 상품으로 들어갈 수 있으므로 다대일 단방향 매핑

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;    // 1개의 주문은 여러 개의 상품을 주문할 수 있으므로 다대일 단방향 매핑

    private int orderPrice; // 주문가격

    private int count;

//    private LocalDateTime regTime;    // BaseEntity 사용으로 제거해도됨
//
//    private LocalDateTime updateTime;
}
