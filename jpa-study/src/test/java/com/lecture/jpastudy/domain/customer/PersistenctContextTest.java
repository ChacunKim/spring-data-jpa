package com.lecture.jpastudy.domain.customer;

import com.lecture.jpastudy.domain.customer.Customer;
import com.lecture.jpastudy.domain.customer.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class PersistenctContextTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    public EntityManagerFactory emf;

    @BeforeEach
    void setUp(){
        repository.deleteAll();
    }

    @Test
    void 저장(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(1L, "hong-gu", "Kim"); //비영속 상태
        entityManager.persist(customer); //비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();
    }

    @Test
    void 조회_DB조회(){
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(1L, "hong-gu", "Kim"); //비영속 상태
        entityManager.persist(customer); //비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        entityManager.detach(customer); // 영속 -> 준영속

        Customer selected = entityManager.find(Customer.class, 1L);
        log.info("{} {}", selected.getFirstName(), selected.getLastName());
    }
    @Test
    void 조회_1차캐시_이용(){ // DB에 SELECT 쿼리 x
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(1L, "hong-gu", "Kim"); //비영속 상태
        entityManager.persist(customer); //비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        Customer selected = entityManager.find(Customer.class, 1L);
        log.info("{} {}", selected.getFirstName(), selected.getLastName());
    }
}
