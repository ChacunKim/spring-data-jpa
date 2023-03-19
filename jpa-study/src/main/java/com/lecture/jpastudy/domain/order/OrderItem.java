package com.lecture.jpastudy.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int price;
    private int quantity;

    //FK
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "item_id")
    private Long itemId;

}
