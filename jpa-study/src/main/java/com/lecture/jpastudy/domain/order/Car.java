package com.lecture.jpastudy.domain.order;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Car extends Item {
    private int power;
}
