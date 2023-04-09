package edu.school21.restful.repository;

import edu.school21.restful.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByFirstname(String firstname);

    @Query("select c.students from Course c where c.id=:courseId")
    List<User> findStudentsByCourse(@Param("courseId") Long courseId, Pageable pageable);

    @Query("select c.teachers from Course c where c.id=:courseId")
    List<User> findTeachersByCourse(@Param("courseId") Long courseId, Pageable pageable);
}
