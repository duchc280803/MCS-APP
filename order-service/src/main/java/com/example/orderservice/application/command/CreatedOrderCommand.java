package com.example.orderservice.application.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedOrderCommand {

    private String productId;

    private int quantity;
}
