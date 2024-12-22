package com.main.studyingplatform.Repository;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.StudentProgress;
import com.main.studyingplatform.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    List<StudentProgress> findByStudentAndCourse(User student, Course course);

    List<StudentProgress> findByStudentAndResource(User student, Resource resource);

    List<StudentProgress> findByStudent(User student);
}
