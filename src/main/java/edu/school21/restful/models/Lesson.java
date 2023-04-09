package edu.school21.restful.models;

import edu.school21.restful.models.dto.LessonDto;

import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import org.hibernate.Hibernate;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Lesson extends RepresentationModel<Lesson> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Date startTime;
    private Date endTime;
    private String dayOfWeek;
    @OneToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    public Lesson(LessonDto lessonDto) {
        this.startTime = lessonDto.getStartTime();
        this.endTime = lessonDto.getEndTime();
        this.dayOfWeek = lessonDto.getDayOfWeek();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Lesson lesson = (Lesson) o;
        return id != null && Objects.equals(id, lesson.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
