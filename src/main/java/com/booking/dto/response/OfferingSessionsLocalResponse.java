package com.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OfferingSessionsLocalResponse {
    private Long offeringId;
    private String offeringTitle;
    private List<SessionLocalTimeResponse> sessions;
}
