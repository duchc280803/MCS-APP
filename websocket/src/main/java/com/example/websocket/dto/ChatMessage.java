package com.example.websocket.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ChatMessage {
    private String sender;
    private String content;
    private String sessionId;
    private Instant timestamp;

}