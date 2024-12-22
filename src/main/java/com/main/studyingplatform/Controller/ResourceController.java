package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.CourseRepository;
import com.main.studyingplatform.Repository.ResourceRepository;
import com.main.studyingplatform.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        System.out.println("Authenticated Teacher: " + teacher.getUsername());

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        System.out.println("Course Creator: " + course.getCreatedBy().getUsername());

        if (!course.getCreatedBy().getId().equals(teacher.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to add resources to this course");
        }

        // File upload logic
        String fileName = fileStorageService.saveFile(file);

        // Save resource
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
        return ResponseEntity.ok(course.getResourceEntities());
    }

    @GetMapping("/video/{fileName}")
    public ResponseEntity<org.springframework.core.io.Resource> getVideo(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), "videos").resolve(fileName).normalize();
            org.springframework.core.io.Resource videoResource = new UrlResource(filePath.toUri());

            if (!videoResource.exists()) {
                throw new RuntimeException("Video file not found: " + fileName);
            }

            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(videoResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(videoResource);

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching video: " + e.getMessage(), e);
        }
    }

}
