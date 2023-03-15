package com.lecture.connectdbbasic.repository.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="customers")
public class CustomerEntity {

    @Id
    private long id;
    private String firstName;
    private String lastName;
    private int age;
}
