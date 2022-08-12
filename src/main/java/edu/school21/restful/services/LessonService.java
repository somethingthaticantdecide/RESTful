package edu.school21.restful.services;

import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.dto.LessonDto;
import edu.school21.restful.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson addCourse(LessonDto lessonDto) {
        Lesson lesson = new Lesson(lessonDto);
        lessonRepository.save(lesson);
        return lesson;
    }
}
