package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        /* 원래는 delivery 따로생성, order 따로 persist 해야되는 됨(deliveryRepository.save() 로 넣어주고 해야됨)
         그러나 order 에서 Cascade 를 사용해서 order 만 해줘도 Cascade 된 객체들도 다같이 persist 됨
         deliver 와 orderItem 이 order 에서만 참조해서 씀!!
         만약 order 외에서 참조하는 곳이 있으면 cascade를 막 사용하면안됨
         왜냐하면 묶여있으니 다같이 삭제가 되거나 다른 예외 상황이 생길 수 있음.*/
        orderRepository.save(order);

        return order.getId();
    }
    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }
    // 검색
    /*public List<Order> findOrders(OrderSerach orderSerach){
        return orderRepository.findAll(orderSerach);
    }*/
}
