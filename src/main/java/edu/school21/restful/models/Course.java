package edu.school21.restful.models;

import edu.school21.restful.models.dto.CourseDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Date startDate;
    private Date endDate;
    private String name;
    @OneToMany
    @ToString.Exclude
    private List<User> teachers;
    @OneToMany
    @ToString.Exclude
    private List<User> students;
    private String description;
    @OneToMany
    @ToString.Exclude
    private List<Lesson> lessons;

    public Course(CourseDto courseDto) {
        this.startDate = courseDto.getStartDate();
        this.endDate = courseDto.getEndDate();
        this.description = courseDto.getDescription();
        this.name = courseDto.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(startDate, course.startDate) && Objects.equals(endDate, course.endDate) && Objects.equals(name, course.name) && Objects.equals(teachers, course.teachers) && Objects.equals(students, course.students) && Objects.equals(description, course.description) && Objects.equals(lessons, course.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, name, teachers, students, description, lessons);
    }
}
