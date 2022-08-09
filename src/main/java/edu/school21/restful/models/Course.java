package edu.school21.restful.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

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
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    @ManyToOne
    private User teachers;
    @ManyToOne
    private User students;
    private String description;
    @ManyToOne
    private Lesson lessons;

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
