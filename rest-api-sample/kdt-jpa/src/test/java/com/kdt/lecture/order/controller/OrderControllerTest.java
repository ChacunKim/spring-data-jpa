package com.kdt.lecture.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt.lecture.domain.order.OrderRepository;
import com.kdt.lecture.domain.order.OrderStatus;
import com.kdt.lecture.item.dto.ItemDto;
import com.kdt.lecture.item.dto.ItemType;
import com.kdt.lecture.member.dto.MemberDto;
import com.kdt.lecture.order.dto.OrderDto;
import com.kdt.lecture.order.dto.OrderItemDto;
import com.kdt.lecture.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        // Given
        OrderDto orderDto = OrderDto.builder()
                .uuid(uuid)
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(
                        MemberDto.builder()
                                .name("강홍구")
                                .nickName("guppy.kang")
                                .address("서울시 동작구만 움직이면 쏜다.")
                                .age(33)
                                .description("---")
                                .build()
                )
                .orderItemDtos(List.of(
                        OrderItemDto.builder()
                                .price(1000)
                                .quantity(100)
                                .itemDtos(List.of(
                                        ItemDto.builder()
                                                .type(ItemType.FOOD)
                                                .chef("백종원")
                                                .price(1000)
                                                .build()
                                ))
                                .build()
                ))
                .build();
        // When
        String uuid = orderService.save(orderDto);

    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void saveCallTest() throws Exception {
        //given
        OrderDto orderDto = OrderDto.builder()
                .uuid(UUID.randomUUID().toString())
                .memo("문앞 보관 해주세요.")
                .orderDatetime(LocalDateTime.now())
                .orderStatus(OrderStatus.OPENED)
                .memberDto(
                        MemberDto.builder()
                                .name("강홍구")
                                .nickName("guppy.hong")
                                .address("서울시 동작구만 움직이면 쏜다.")
                                .age(33)
                                .description("---")
                                .build()
                )
                .orderItemDtos(List.of(
                        OrderItemDto.builder()
                                .price(1000)
                                .quantity(100)
                                .itemDtos(List.of(
                                        ItemDto.builder()
                                                .type(ItemType.FOOD)
                                                .chef("백종원")
                                                .price(1000)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        //When //Then
        // mockMVC를 통해 실제 endpoint url request 를 요청해볼 수 있음
        // orderDto: request body
        // post("/orders"): post 요청
        // .contentType(MediaType.APPLICATION_JSON) >>> json 형태
        // .content(objectMapper.writeValueAsString(orderDto))) >>> orderDto를 json 형식의 string으로 변환
        // .andExpect(status().isOk()) >>> 200 예상
        // .andDo(print()) >>> 프린트
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andDo(document("order-save",
                        requestFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID"),
                                fieldWithPath("orderDatetime").type(JsonFieldType.STRING).description("orderDatetime"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("orderStatus"),
                                fieldWithPath("memo").type(JsonFieldType.STRING).description("memo"),
                                fieldWithPath("memberDto").type(JsonFieldType.OBJECT).description("memberDto"),
                                fieldWithPath("memberDto.id").type(JsonFieldType.NULL).description("memberDto.id"),
                                fieldWithPath("memberDto.name").type(JsonFieldType.STRING).description("memberDto.name"),
                                fieldWithPath("memberDto.nickName").type(JsonFieldType.STRING).description("memberDto.nickName"),
                                fieldWithPath("memberDto.age").type(JsonFieldType.NUMBER).description("memberDto.age"),
                                fieldWithPath("memberDto.address").type(JsonFieldType.STRING).description("memberDto.address"),
                                fieldWithPath("memberDto.description").type(JsonFieldType.STRING).description("memberDto.description"),
                                fieldWithPath("orderItemDtos[]").type(JsonFieldType.ARRAY).description("orderItemDtos[]"),
                                fieldWithPath("orderItemDtos[].id").type(JsonFieldType.NULL).description("orderItemDtos[].id"),
                                fieldWithPath("orderItemDtos[].price").type(JsonFieldType.NUMBER).description("orderItemDtos[].price"),
                                fieldWithPath("orderItemDtos[].quantity").type(JsonFieldType.NUMBER).description("orderItemDtos[].quantity"),
                                fieldWithPath("orderItemDtos[].itemDtos[]").type(JsonFieldType.ARRAY).description("orderItemDtos[].itemDtos"),
                                fieldWithPath("orderItemDtos[].itemDtos[].price").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDtos[].price"),
                                fieldWithPath("orderItemDtos[].itemDtos[].stockQuantity").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDtos[].stockQuantity"),
                                fieldWithPath("orderItemDtos[].itemDtos[].type").type(JsonFieldType.STRING).description("orderItemDtos[].itemDtos[].type"),
                                fieldWithPath("orderItemDtos[].itemDtos[].chef").type(JsonFieldType.STRING).description("orderItemDtos[].itemDtos[].chef")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("데이터"),
                                fieldWithPath("serverDatetime").type(JsonFieldType.STRING).description("응답시간")
                        )));
        /*
        * document(): rest docs 의 method
        * identifier: "order-save":: 문서 이름
        * requestFields: 요청값 >> path로 uuid가 string으로 넘어갈 것 / 설명(description): UUID + 등등 ...
        * responseFields: 응답값 >> 상태코드, 데이터, 응답시간
        *
        * org.springframework.restdocs.snippet.SnippetException: The following parts of the payload were not documented:
        * >> 문서화 되어야 할 필드들이 문서화되지 않았다.
        * >> rest docs 문서와 값이 항상 같도록(최신 상태를 유지하도록) 오류 메세지를 보낸다.
        * >> test 성공 시 target 폴더의 generated-snippets 에 docs 가 생김.
        * >>> kdt-jpa.src.main.asciidoc.index.adoc 파일에 문서 정리
        * >>> maven plugins, asciidoctor:process-asciidoc 실행
        *       -> target 폴더의 generated-docs 에 index.xml 파일이 생김.
        *
        * Swagger: 운영 코드에 documentation 코드가 섞임. 읽기 힘들고 유지 보수 측면에서 좋지 않다.
        *       + API에 대한 검증이 자동적으로 되지 않음.
        * */
    }

    @Test
    void getOne() throws Exception {
        mockMvc.perform(get("/orders/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/orders")
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(10))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

}