package edu.school21.restful.models.dto;

import lombok.Data;

@Data
public class LessonDto {
    private String startTime;
    private String endTime;
    private String dayOfWeek;
    private String teacher;
}
