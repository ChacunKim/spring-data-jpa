package com.kdt.lecture.order.controller;

import com.kdt.lecture.ApiResponse;
import com.kdt.lecture.order.dto.OrderDto;
import com.kdt.lecture.order.service.OrderService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ExceptionHandler
    private ApiResponse<String> exceptionHandle(Exception exception) {
        return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    private ApiResponse<String> notFoundHandler(NotFoundException exception) {
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @PostMapping("/orders")
    public ApiResponse<String> save(
            @RequestBody OrderDto orderDto
    ) {
        return ApiResponse.ok(orderService.save(orderDto));
    }

    @GetMapping("/orders/{uuid}")
    public ApiResponse<OrderDto> getOne (
            @PathVariable String uuid
    ) throws NotFoundException {
        return ApiResponse.ok(orderService.findOne(uuid));
    }


    @GetMapping("/orders")
    public ApiResponse<Page<OrderDto>> getAll (
            Pageable pageable
    ) {
        return ApiResponse.ok(orderService.findAll(pageable));
    }



    @PostMapping("/orders/{uuid}")
    public ApiResponse<String> update(
            @PathVariable String uuid,
            @RequestBody OrderDto orderDto
    ) throws NotFoundException {
        return ApiResponse.ok(orderService.update(uuid, orderDto));
    }
}
