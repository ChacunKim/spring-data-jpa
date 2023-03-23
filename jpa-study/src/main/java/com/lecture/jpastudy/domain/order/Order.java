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
        - LAZY: 필요할 때 가져옴 >> 프록시 객체. getMember를 했을 때 비로소 select 쿼리를 통해 members 테이블에서 객체를 가져온다.
        - EAGER: 조회할 때 같이 가져옴 >> entity 객체: order를 가져올 때 member도 entity 객체로 가져옴(프록시가 아니라)
        */

    @ManyToOne(fetch = FetchType.EAGER) //다대일(N:1) 관계 -> 주문(N) : 회원(1)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // flush 순간 RDS(DB)에서도 remove 하겠다.
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
