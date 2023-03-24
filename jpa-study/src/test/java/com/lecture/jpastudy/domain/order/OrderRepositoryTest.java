package com.lecture.jpastudy.domain.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void test(){
        String uuid = UUID.randomUUID().toString();

        Order order = new Order();
        order.setId(uuid);
        order.setOrderStatus(OrderStatus.OPENED);
        order.setOrderDateTime(LocalDateTime.now());
        order.setMemo("----");
        order.setCreatedBy("anonymous");
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        Order order1 = orderRepository.findById(uuid).get();
        List<Order> all = orderRepository.findAll();

        //method query
        orderRepository.findAllByOrderStatus(OrderStatus.OPENED);
        orderRepository.findAllByOrderStatusOrderByOrderDateTime(OrderStatus.OPENED);

        //custom query
        orderRepository.findByMemo("----");
    }
}