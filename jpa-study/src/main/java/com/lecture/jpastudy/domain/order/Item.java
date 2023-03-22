package com.lecture.jpastudy.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
//@Inheritance(strategy = InheritanceType.JOINED) //조인 테이블 전략
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //싱글 테이블 전략
@DiscriminatorColumn(name = "DTYPE") // 싱글 테이블 전략: "DTYPE"이 구분자가 된다. :현업에서 많이 사용. 조인테이블은 관리가 복잡.
@Table(name = "item")
public abstract class Item extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int price;
    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "order_item_id", referencedColumnName = "id")
    private OrderItem orderItem;

    public void setOrderItem(OrderItem orderItem) {
        if (Objects.nonNull(this.orderItem)) {
            this.orderItem.getItems().remove(this);
        }

        this.orderItem = orderItem;
        orderItem.getItems().add(this);
    }


}