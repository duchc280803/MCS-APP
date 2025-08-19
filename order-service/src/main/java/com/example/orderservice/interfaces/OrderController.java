package com.example.orderservice.interfaces;

import com.example.orderservice.application.command.CreatedOrderCommand;
import com.example.orderservice.application.service.OrderService;
import com.example.orderservice.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order/")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("create")
    public CommonResponse createOrder(@RequestBody CreatedOrderCommand req) {
        return orderService.createdOrder(req);
    }
}
