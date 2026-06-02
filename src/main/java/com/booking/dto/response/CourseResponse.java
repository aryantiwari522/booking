package com.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private Long teacherId;
}
