package edu.school21.restful.models.dto;

import lombok.Data;

@Data
public class CourseDto {
    private String startDate;
    private String endDate;
    private String name;
    private String teachers;
    private String students;
    private String description;
    private String lessons;
}
