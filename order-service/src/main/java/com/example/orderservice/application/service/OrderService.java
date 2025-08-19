package com.example.orderservice.application.service;

import com.example.orderservice.application.command.CreatedOrderCommand;
import com.example.orderservice.shared.CommonResponse;

public interface OrderService {

    CommonResponse createdOrder(CreatedOrderCommand createdOrderCommand);
}
