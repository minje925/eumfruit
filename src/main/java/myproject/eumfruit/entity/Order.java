package myproject.eumfruit.entity;

import lombok.Getter;
import lombok.Setter;
import myproject.eumfruit.constant.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    // cascade 연속성전이 설정, 부모 엔티티의 변화에 자식 엔티티에 모두 전이되는 옵션이 ALL
    // orphanRemoval = true : 고아 객체 제거, 주문엔티티에서 주문 상품을 삭제했을때, orderItem 엔티티가 삭제되어야함, orderitem을 삭제하면 부모엔티티와 연관관계가 끊어지면 고아객체를 삭제함
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)          // order_item과 일대다 매핑, order_id외래키가 oder_item 테이블에 있기 때문에 연관관계 주인은 order_item이다 그래서 변경해줘야한다. => mappedBy로 연관관계의 주인을 설정한다.
    private List<OrderItem> orderItems = new ArrayList<>();
}
