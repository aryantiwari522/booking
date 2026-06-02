package com.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SessionRequest {
    @NotBlank
    private String startLocal; // yyyy-MM-ddTHH:mm

    @NotBlank
    private String endLocal;
}
