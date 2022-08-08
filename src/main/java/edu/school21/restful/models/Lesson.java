package edu.school21.restful.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
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
}
