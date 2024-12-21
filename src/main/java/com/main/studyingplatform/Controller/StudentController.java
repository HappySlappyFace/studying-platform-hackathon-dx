package com.main.studyingplatform.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
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
}
