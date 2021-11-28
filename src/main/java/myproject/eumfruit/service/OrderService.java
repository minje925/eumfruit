package myproject.eumfruit.service;
import lombok.RequiredArgsConstructor;
import myproject.eumfruit.Repository.ItemRepository;
import myproject.eumfruit.Repository.MemberRepository;
import myproject.eumfruit.Repository.OrderRepository;
import myproject.eumfruit.dto.OrderDto;
import myproject.eumfruit.entity.Item;
import myproject.eumfruit.entity.Member;
import myproject.eumfruit.entity.Order;
import myproject.eumfruit.entity.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new); // 주문할 상품을 조회
        Member member = memberRepository.findByEmail(email);    // 현재 로그인한 회원의 이메일 정보를 회원 정보에서 조회한다.

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList); // 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
        orderRepository.save(order);    //  생성한 주문 엔티티 저장

        return order.getId();
    }
}
