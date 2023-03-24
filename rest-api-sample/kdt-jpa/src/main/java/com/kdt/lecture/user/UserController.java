package com.kdt.lecture.user;

public class UserController {
    /* 게시판 과제
    * datasource: h2 or mysql
    *
    * 엔티티
    * 회원(User)
    *   - id(PK auto increment), name, age, hobby, created_at, created_by
    * 게시글(Post)
    *   - id(PK auto increment), title, content, created_at, created_by
    * >> created_at과 created_by는 @MappedSuperclass BaseEntity 에서 받아오기
    * >> 회원과 게시글에 대한 연관관계: 회원(1) : 게시글(N)
    * >> 게시글 repository 구현(PostRepository)
    *
    * API 구현
    * 게시글 조회
    *   - 페이징 조회(GET "/posts")
    *   - 단건 조회(GET "/posts/{id}")
    * 게시글 작성 (POST "/posts)
    * 게시글 수정 (POST "/posts/{id}")
    *
    * REST DOCS 를 이용해 문서화
    *
    * */
}
