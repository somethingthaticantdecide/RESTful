package edu.school21.restful.models.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CourseDto {
    private Date startDate;
    private Date endDate;
    private String name;
    private String description;
}
