package com.lecture.jpastudy.domain.order;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Furniture extends Item{
    private int width;
    private int height;
}
