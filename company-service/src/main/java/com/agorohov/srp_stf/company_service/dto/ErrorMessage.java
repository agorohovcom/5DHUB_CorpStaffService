package com.agorohov.srp_stf.company_service.dto;

import java.time.LocalDateTime;

public class ErrorMessage {

    private String message;
    private LocalDateTime time;

    public ErrorMessage() {
    }

    public ErrorMessage(String message, LocalDateTime time) {
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
