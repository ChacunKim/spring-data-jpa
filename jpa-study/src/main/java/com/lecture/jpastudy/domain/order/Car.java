package com.lecture.jpastudy.domain.order;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("CAR") //싱글 테이블 전략에 사용
public class Car extends Item {
    private int power;
}
