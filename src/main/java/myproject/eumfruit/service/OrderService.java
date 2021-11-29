package myproject.eumfruit.service;
import lombok.RequiredArgsConstructor;
import myproject.eumfruit.Repository.ItemImgRepository;
import myproject.eumfruit.Repository.ItemRepository;
import myproject.eumfruit.Repository.MemberRepository;
import myproject.eumfruit.Repository.OrderRepository;
import myproject.eumfruit.dto.OrderDto;
import myproject.eumfruit.dto.OrderHistDto;
import myproject.eumfruit.dto.OrderItemDto;
import myproject.eumfruit.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

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

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        List<Order> orders = orderRepository.findOrders(email, pageable);   // 유저의 아이디와 페이징 조건을 이용하여 주문 목록의 조회
        Long totalCount = orderRepository.countOrder(email);    // 유저의 주문 총 개수를 구한다.

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for(Order order : orders) { // 주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO를 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems) {
                Itemimg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y"); // 주문한 상품의 대표이미지 조회
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount); // 페이지 구현 객체를 생성하여 반환환
    }
    
    @Transactional(readOnly = true)
    public Order getByOrderId(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        Order result = order.get();
        return result;
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        // 현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사한다.
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
        // 주문 취소 상태로 변경하면 변경 감지 기능에 의해서 트랜잭션이 끝날 때 update쿼리가 실행된다.
    }

}
