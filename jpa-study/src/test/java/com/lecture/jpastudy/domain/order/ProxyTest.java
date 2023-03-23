package com.lecture.jpastudy.domain.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class ProxyTest {

    @Autowired
    EntityManagerFactory emf;

    private String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp(){
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        //주문 엔티티
        Order order = new Order();
        order.setId(uuid);
        order.setMemo("부재시 전화주세요");
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        order.setOrderStatus(OrderStatus.OPENED);

        em.persist(order);

        //회원 엔티티
        Member member = new Member();
        member.setName("홍길동");
        member.setNickName("동동이");
        member.setAge(33);
        member.setAddress("대전시 동그리동동");
        member.setDescription("나는 의적");

        member.addOrder(order); //연관관계 편의 메소드 사용
        em.persist(member);
        transaction.commit();
    }

    @Test
    void proxy(){
        EntityManager entityManager = emf.createEntityManager();
        Order order = entityManager.find(Order.class, uuid);

        Member member = order.getMember();

        // order 객체로부터 가져온 member 객체가 사용 전(get 등) 아직 프록시 객체인지 확인
        // order entity의 member 필드에 매핑된 fetch 전략이 LAZY이므로, member 객체가 사용되기 전이라면 isLoaded가 false.
        log.info("MEMBER USE BEFORE IS-LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member)); //false: member 객체는 proxy 객체이다.

        String nickName = member.getNickName(); // member 객체 사용
        log.info("MEMBER USE AFTER IS-LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member)); //true: member 객체는 entity 객체이다.

    }

    @Test
    void move_persist(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Order order = entityManager.find(Order.class, uuid);

        transaction.begin();

        /*
            1) Order 앤티티의 order item: cascade 지정 x >> 영속성 전이 x
            @OneToMany(mappedBy = "order")
            private List<OrderItem> orderItems;

        * */

        OrderItem item = new OrderItem();
        item.setQuantity(10); // 준영속
        item.setPrice(1000); // 준영속

        /*
            - 영속 상태인 order 에 item을 추가 : item이 아직 준영속 상태이기 때문에
            OrderItem에 대한 insert 쿼리가 실행되지 않는다.
        */
        order.addOrderItem(item);
        transaction.commit();

        /*
            2) Order 앤티티의 order item: cascade 지정: ALL >> 영속성 전이 O
            @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
            private List<OrderItem> orderItems;
            >> OrderItem에 대한 insert 쿼리가 실행된다.
        */
    }

    @Test
    void testOrphanRemoval(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Order order = entityManager.find(Order.class, uuid);

        transaction.begin();

        OrderItem item = new OrderItem();
        item.setQuantity(10);
        item.setPrice(1000);

        order.addOrderItem(item);
        transaction.commit(); //flush
        entityManager.clear();
        //////////////////////////////////////////////////////////////////
        Order order2 = entityManager.find(Order.class, uuid); //영속 상태

        transaction.begin();

        order2.getOrderItems().remove(0); // 고아 상태
        /*
          지워진 orderItem 0번은 order2와의 연결이 끊어졌기 때문에 고아 상태이다.
          따라서, commit 시 delete 쿼리가 실행되지 않고, 테이블에서도 해당 order와 매핑된 orderItem 0번이 삭제되지 않는다.
          테이블에서 삭제되지 않았기 때문에 다음 번에 동일 id의 order를 조회했을 시, orderItem 0번이 다시 조회된다.
           >> orphanRemoval = true 옵션으로 해결
        */
        transaction.commit(); // flush
    }
}
