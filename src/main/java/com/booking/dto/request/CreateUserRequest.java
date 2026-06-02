package com.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    private String name;

    private String timezone; // IANA zone ID , e.g. "Asia/Kolkata"
}
