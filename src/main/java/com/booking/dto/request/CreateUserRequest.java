package com.booking.dto.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
        private String timezone; // IANA zone ID , e.g. "Asia/Kolkata"
}
