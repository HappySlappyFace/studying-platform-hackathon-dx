package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.CourseRepository;
import com.main.studyingplatform.Repository.ResourceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private ResourceRepository resourceRepository;
    private CourseRepository courseRepository;

    @PostMapping("/upload-resource")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> uploadResource(@RequestBody Map<String, String> resource) {
        // Simulate resource upload
        return ResponseEntity.ok("Resource uploaded successfully");
    }

    @GetMapping("/my-resources")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<String>> getResources() {
        // Simulate fetching teacher resources
        List<String> resources = List.of("Resource 1", "Resource 2");
        return ResponseEntity.ok(resources);
    }


    @GetMapping("/my-courses")
    public ResponseEntity<List<Course>> getMyCourses(@AuthenticationPrincipal User teacher) {
        List<Course> courses = courseRepository.findByCreatedBy(teacher);
        return ResponseEntity.ok(courses);
    }


}
