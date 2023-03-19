package com.lecture.jpastudy.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class OrderPersistenceTest {

    @Autowired
    EntityManagerFactory emf;

    /**
     * Hibernate: drop table if exists customers cascade
     * Hibernate: drop table if exists member cascade
     * Hibernate: drop sequence if exists member_SEQ
     * Hibernate: create sequence member_SEQ start with 1 increment by 50
     * Hibernate: create table customers (id bigint not null, firstName varchar(255), lastName varchar(255), primary key (id))
     * Hibernate: create table member (id bigint not null, address varchar(255) not null, age integer not null, description varchar(255) not null, name varchar(30), nick_name varchar(30) not null, primary key (id))
     * Hibernate: alter table if exists member add constraint UK_f3xpkeiwuq8kwkt45lkvanwsd unique (nick_name)
     * */
    @Test
    void member_insert(){
        Member member = new Member();
        member.setName("홍길동");
        member.setAddress("대전광역시 어쩌구");
        member.setAge(30);
        member.setNickName("gildong");
        member.setDescription("의적입니다");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member);

        transaction.commit();
    }

    @Test
    void 잘못된_설계(){
        Member member = new Member();
        member.setName("홍길동");
        member.setAddress("대전광역시 어쩌구");
        member.setAge(30);
        member.setNickName("gildong");
        member.setDescription("의적입니다");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(member);
        Member memberEntity = entityManager.find(Member.class, 1L);

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo("부재 시 전화주세요.");
        order.setMemberId(memberEntity.getId()); //외래키를 직접 지정

        entityManager.persist(order);

        transaction.commit();

        Order orderEntity = entityManager.find(Order.class, order.getId()); // select order

        //FK를 이용해 회원 다시 조회: 조회가 두 번 일어남. 알아서 가져와야 하지 않을까
        Member orderMemberEntity = entityManager.find(Member.class, orderEntity.getMemberId()); //select member

        //orderEntity.getMember() -> 객체 중심 설계라면 객체 그래프 탐색을 해야하지 않을까?
        // 연관관계 맵핑: 주문 객체를 통해 회원 객체를 가져올 수 있고, 회원 객체를 통해 여러 주문 객체를 가져올 수 있음.
        log.info("nick : {}", orderMemberEntity.getNickName());
    }

    @Test
    void 단방향_테스트(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Member member = new Member();
        member.setName("홍길동");
        member.setAddress("대전광역시 어쩌구");
        member.setAge(30);
        member.setNickName("gildong");
        member.setDescription("의적입니다");

        entityManager.persist(member);

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo("부재 시 전화주세요.");
        order.setMember(member); //Member 객체와 매핑
//        member.setOrders(Lists.newArrayList(order));

        entityManager.persist(order);
        transaction.commit();

        entityManager.clear();
        Order entity = entityManager.find(Order.class, order.getId());

        log.info("{}", entity.getMember().getName()); // 객체 그래프 탐색: 자유롭게 회원 정보를 가져올 수 있다.
        log.info("{}",entity.getMember().getOrders().size()); //양방향 매핑: member 객체를 통해 order 객체를 가져올 수 있음.
        log.info("{}", order.getMember().getOrders().size()); // member에 orders를 넣지 않으면 사이즈가 0. 양방향 매핑이면 양쪽에 모두 setting해줘야 함.
    }
}
