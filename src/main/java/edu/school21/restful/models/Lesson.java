package edu.school21.restful.models;

import edu.school21.restful.models.dto.LessonDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Lesson {
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

    }
}
