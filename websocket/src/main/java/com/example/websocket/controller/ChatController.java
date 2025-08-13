package com.example.websocket.controller;

import com.example.websocket.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

    @Controller
    public class ChatController {

        @MessageMapping("/chat") // Gửi từ client: /app/chat
        @SendTo("/topic/messages") // Gửi lại cho tất cả sub ở /topic/messages
        public ChatMessage send(ChatMessage message) {
            message.setContent("🗨️ " + message.getSender() + ": " + message.getContent());
            return message;
        }
}
