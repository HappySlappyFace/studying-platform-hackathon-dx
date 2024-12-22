package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;



@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@RequestBody Course course,
                                               @AuthenticationPrincipal User teacher) {
        course.setCreatedBy(teacher);
        courseRepository.save(course);
        return ResponseEntity.ok("Course created successfully");
    }
}
