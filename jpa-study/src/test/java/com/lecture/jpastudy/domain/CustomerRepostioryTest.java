package com.lecture.jpastudy.domain;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class CustomerRepostioryTest {

    @Autowired
    private CustomerRepository repository;


    @Test
    void test(){
        // Given
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Gildong");
        customer.setLastName("Hong");

        //When
        repository.save(customer);

        //Then
        Customer entity = repository.findById(1L).get();
        log.info("customer: {} {} == entity: {} {}", customer.getFirstName(), customer.getLastName(), entity.getFirstName(), entity.getLastName());
    }

}