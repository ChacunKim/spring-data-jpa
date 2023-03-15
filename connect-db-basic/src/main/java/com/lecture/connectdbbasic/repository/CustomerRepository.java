package com.lecture.connectdbbasic.repository;

import com.lecture.connectdbbasic.repository.domain.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

}
