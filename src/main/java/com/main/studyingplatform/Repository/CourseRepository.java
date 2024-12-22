package com.main.studyingplatform.Repository;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCreatedBy(User teacher);
}

