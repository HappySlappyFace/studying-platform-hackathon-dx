package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/recommendations")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<String>> getRecommendations() {
        // Simulate recommendations
        List<String> recommendations = List.of("Course 1", "Course 2", "Course 3");
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<String> getStudentProfile() {
        return ResponseEntity.ok("This is the student profile");
    }

    // Get All Courses (since all are public)
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    // Get Course Details by ID (with resources)
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return ResponseEntity.ok(course);
    }
}
