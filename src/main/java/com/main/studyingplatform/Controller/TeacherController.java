package com.main.studyingplatform.Controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

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
}
