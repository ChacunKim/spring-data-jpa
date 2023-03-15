package com.lecture.connectdbbasic;

import com.lecture.connectdbbasic.repository.CustomerRepository;
import com.lecture.connectdbbasic.repository.domain.CustomerEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class JPATest {

    @Autowired
    CustomerRepository repository;

    @BeforeEach
    void setUp(){}

    @AfterEach
    void teatDown(){
        repository.deleteAll();
    }

    @Test
    void INSET_TEST(){
        //Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("gildong");
        customer.setLastName("hong");
        customer.setAge(45);

        //when
        repository.save(customer);

        //Then
        CustomerEntity entity = repository.findById(1L).get();
        log.info("Full name: {} {}", entity.getFirstName(), entity.getLastName());
    }

    @Test
    @Transactional //영속성 컨텍스트 안에서 관리
    void UPDATE_TEST(){
        //Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setFirstName("gildong");
        customer.setLastName("hong");
        customer.setAge(45);
        repository.save(customer);

        //When  :: 영속성 컨텍스트 관리: 변경 감지, 자동으로 update 쿼리 실행
        CustomerEntity entity = repository.findById(1L).get();
        entity.setFirstName("guppy");
        entity.setLastName("hong");

        //Then: 업데이트 후 조회되었는지
        //jpa는 객체와 table이 맵핑. 객체가 변경되면 table도 변경된다.
        //ORM 프레임워크: 객체와 관계를 맵핑시켜주는 프레임워크. (Object Relation Mapper)
        CustomerEntity updated = repository.findById(1L).get();
        log.info("Full name: {} {}", entity.getFirstName(), entity.getLastName());


    }

}
