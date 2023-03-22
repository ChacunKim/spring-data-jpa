package com.lecture.jpastudy.domain.order;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Food extends Item{
    private String chef;
}
