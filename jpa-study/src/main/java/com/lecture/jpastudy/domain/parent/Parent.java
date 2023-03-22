package com.lecture.jpastudy.domain.parent;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
//@IdClass(ParentId.class) // 식별자로 IdClass를 사용할 것임.
public class Parent {
/*    @Id
    private String id1;
    @Id
    private String id2;*/

    @EmbeddedId
    private ParentId parentId;
}
