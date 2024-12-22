package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.CourseRepository;
import com.main.studyingplatform.Repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/my-resources")
    public ResponseEntity<List<Resource>> getResources(@AuthenticationPrincipal User teacher) {
        List<Resource> resourceEntities = resourceRepository.findByUploadedBy(teacher);
        return ResponseEntity.ok(resourceEntities);
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyCourses(@AuthenticationPrincipal User teacher) {
        List<Course> courses = courseRepository.findByCreatedBy(teacher);
        return ResponseEntity.ok(courses);
    }
}
