package edu.school21.restful.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LessonDto {
    private Date startTime;
    private Date endTime;
    private String dayOfWeek;
    private String teacher;
}
