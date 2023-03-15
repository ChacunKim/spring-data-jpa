package com.lecture.connectdbbasic.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@AllArgsConstructor
@Alias("customers")
public class Customer {
    private long id;
    private String firstName;
    private String lastName;


}
