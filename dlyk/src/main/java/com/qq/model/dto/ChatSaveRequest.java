package com.qq.model.dto;

import lombok.Data;

@Data
public class ChatSaveRequest {
    private String sessionId;
    private String text;
    private boolean isUser;
}
