package myproject.eumfruit.dto;

import lombok.Getter;
import lombok.Setter;
import myproject.eumfruit.constant.OrderStatus;
import myproject.eumfruit.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    private Long orderId;

    private String orderDate;

    private OrderStatus orderStatus;

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    // 주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        // orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드
        orderItemDtoList.add(orderItemDto);
    }
}
