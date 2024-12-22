package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.CourseRepository;
import com.main.studyingplatform.Repository.ResourceRepository;
import com.main.studyingplatform.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private FileStorageService fileStorageService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadResource(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("description") String description,
                                                 @RequestParam Long courseId,
                                                 @AuthenticationPrincipal User teacher) throws IOException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (!course.getCreatedBy().equals(teacher)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add resources to this course");
        }

        String fileName = fileStorageService.saveFile(file);

        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setFilePath(fileName);
        resource.setCourse(course);
        resource.setUploadedBy(teacher);
        resourceRepository.save(resource);

        return ResponseEntity.ok("Resource uploaded successfully");
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Resource>> getResourcesByCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return ResponseEntity.ok(course.getResources());
    }

    @GetMapping("/my-resources")
    public ResponseEntity<List<Resource>> getResourcesByTeacher(@AuthenticationPrincipal User teacher) {
        List<Resource> resources = resourceRepository.findByUploadedBy(teacher);
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/add-to-course")
    public ResponseEntity<String> addResourceToCourse(@RequestParam Long courseId,
                                                      @RequestBody Resource resource,
                                                      @AuthenticationPrincipal User teacher) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (!course.getCreatedBy().equals(teacher)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the creator of this course");
        }

        resource.setUploadedBy(teacher);
        resource.setCourse(course);
        resourceRepository.save(resource);

        return ResponseEntity.ok("Resource added to course successfully");
    }
}
