package com.lecture.jpastudy.domain.order;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("FURNITURE") //싱글 테이블 전략에 사용
public class Furniture extends Item{
    private int width;
    private int height;
}
