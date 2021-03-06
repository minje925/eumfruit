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
public class Order extends BaseEntity {


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

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);  // 주문 상품 정보들을 담아준다.
        orderItem.setOrder(this);   // Order와 OrderItem은 양방향 참조 관계이므로, orderItem에도 order객체를 세팅
    }
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);    // 상품을 주문한 회원의 정보를 세팅
        for(OrderItem orderItem : orderItemList) {  // 여러 개의 상품을 동시에 주문할 수 있으니, 리스트 형태로 객체를 추가
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);    // 주문 상태 세팅
        order.setOrderDate(LocalDateTime.now());    // 현재 시간을 주문 시간으로 세팅
        return order;
    }

    public int getTotalPrice()  {
        // 주문 전체 금액을 구하는 메소드
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
