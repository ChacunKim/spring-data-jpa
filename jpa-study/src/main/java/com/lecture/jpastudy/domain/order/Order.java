package com.lecture.jpastudy.domain.order;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDateTime;

    @Enumerated(value = EnumType.STRING) //ORDINAL: 숫자 1, 2.. 로 저장 | STRING: 문자열 그대로 저장
    private OrderStatus orderStatus;

    @Lob
    private String memo;

    //member_FK
    @Column(name = "member_id", insertable = false, updatable = false) // persist 시 회원 id에 대하여 update 쿼리 실행 x
    private Long memberId;

    /*
    FetchType
        - LAZY: 필요할 때 가져옴
        - EAGER: 조회할 때 같이 가져옴
        */

    @ManyToOne(fetch = FetchType.LAZY) //다대일(N:1) 관계 -> 주문(N) : 회원(1)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    //연관관계 편의 method
    public void setMember(Member member){
        if (Objects.nonNull(this.member)){
            member.getOrders().remove(this);
        }

        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);
    }

}
