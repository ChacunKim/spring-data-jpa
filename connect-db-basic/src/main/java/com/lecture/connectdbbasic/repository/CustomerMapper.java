package com.lecture.connectdbbasic.repository;

import com.lecture.connectdbbasic.repository.domain.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper {
    void save(Customer customer);
    Customer findById(long id);

}
