package com.ebac.modulo45.controller;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse implements Response {
    private List<String> messages;

    public ErrorResponse() {
        this.messages = new ArrayList<>();
    }

    public ErrorResponse(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }
}
