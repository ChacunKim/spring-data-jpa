package com.kdt.lecture.order.service;

import com.kdt.lecture.domain.order.Order;
import com.kdt.lecture.domain.order.OrderRepository;
import com.kdt.lecture.order.converter.OrderConverter;
import com.kdt.lecture.order.dto.OrderDto;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private final OrderConverter orderConverter;

    @Autowired
    private final OrderRepository orderRepository;

    @Transactional //entityManager 를 자동으로 만들고 영속성 컨텍스트 관리
    public String save (OrderDto orderDto) {
        //1. dto -> entity 변환 (준영속)
        Order order = orderConverter.convertOrder(orderDto);
        //2. 영속화
        Order saved = orderRepository.save(order);
        //2. 결과 반환
        return saved.getUuid(); //꼭 필요한 값, id값만 반환.
    }

    //단건 조회
    @Transactional
    public OrderDto findOne (String uuid) throws NotFoundException {
        //1. 조회를 위한 키값 인자로 받기
        //2. orderRepository.findById(uuid) -> 조회(영속화된 엔티티)
        //3. entity -> dto :: entity객체가 빠져나가면 의도치 않게 쿼리가 실행될 수 있으므로 dto를 사용

        return orderRepository.findById(uuid)
                .map(orderConverter::convertOrderDto)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));
    }

    //페이지 조회
    @Transactional
    public Page<OrderDto> findAll(Pageable pageable) {
        //findAll인자로 pageable을 넣으면 페이지로 조회한다.
        return orderRepository.findAll(pageable)
                .map(orderConverter::convertOrderDto);
    }

    public String update(String uuid, OrderDto orderDto) throws NotFoundException {
        Order order = orderRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));

        order.setMemo(orderDto.getMemo());
        order.setOrderStatus(order.getOrderStatus());

        return order.getUuid();
    }



}
