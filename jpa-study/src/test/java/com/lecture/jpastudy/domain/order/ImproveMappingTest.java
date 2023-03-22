package com.lecture.jpastudy.domain.order;

import com.lecture.jpastudy.domain.parent.Parent;
import com.lecture.jpastudy.domain.parent.ParentId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class ImproveMappingTest {

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void inheritance_test(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Food food = new Food();
        food.setPrice(2000);
        food.setStockQuantity(200);
        food.setChef("백종원");

        entityManager.persist(food);

        transaction.commit();
    }

    @Test
    void mappedSuperClassTest(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo("---");
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        //mappedSuperClass field
        order.setCreatedBy("길동이");
        order.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        entityManager.persist(order);

        transaction.commit();
    }

    @Test
    void id_test(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Parent parent = new Parent();
//        parent.setId1("id1");
//        parent.setId2("Id2");
        parent.setParentId(new ParentId("id1", "id2"));


        entityManager.persist(parent);
        transaction.commit();

        //조회
        entityManager.clear();
        Parent parent1 = entityManager.find(Parent.class, new ParentId("id1", "id2"));
//        log.info("{} {}", parent1.getId1(), parent1.getId2());
        log.info("{}", parent1.getParentId());
    }


}
