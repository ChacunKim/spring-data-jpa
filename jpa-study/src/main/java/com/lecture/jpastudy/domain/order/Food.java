package com.lecture.jpastudy.domain.order;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("FOOD") //싱글 테이블 전략에 사용
public class Food extends Item{
    private String chef;
}
