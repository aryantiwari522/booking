package com.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseWithTeacherResponse {
    private Long courseId;
    private String courseName;
    private Long teacherId;
    private String teacherName;
}
