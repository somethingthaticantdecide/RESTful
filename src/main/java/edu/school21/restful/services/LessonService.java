package edu.school21.restful.services;

import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.dto.LessonDto;
import edu.school21.restful.repository.LessonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public Page<Lesson> getLessonsByCourse(Long id, Pageable pageable) {
        List<Lesson> lessons = lessonRepository.findLessonsByCourse(id, pageable);
        return new PageImpl<>(lessons, pageable, lessons.size());
    }
}
