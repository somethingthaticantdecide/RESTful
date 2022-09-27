package edu.school21.restful.models;

import edu.school21.restful.models.enums.State;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Course extends RepresentationModel<Course> {
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
    @Enumerated(EnumType.STRING)
    private State state;

    public Course(Date startDate, Date endDate, String name, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.description = description;
        this.state = State.DRAFT;
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
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
