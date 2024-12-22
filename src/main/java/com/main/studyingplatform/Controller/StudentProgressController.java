package com.main.studyingplatform.Controller;

import com.main.studyingplatform.DTO.ProgressDTO;
import com.main.studyingplatform.Entities.Course;
import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.StudentProgress;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.CourseRepository;
import com.main.studyingplatform.Repository.ResourceRepository;
import com.main.studyingplatform.Repository.StudentProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student/progress")
public class StudentProgressController {

    @Autowired
    private StudentProgressRepository progressRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @PostMapping("/mark-completed")
    public ResponseEntity<String> markResourceAsCompleted(
            @RequestParam Long courseId,
            @RequestParam Long resourceId,
            @AuthenticationPrincipal User student) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (!course.getResources().contains(resource)) {
            return ResponseEntity.badRequest().body("Resource does not belong to the specified course");
        }

        StudentProgress progress = new StudentProgress();
        progress.setStudent(student);
        progress.setCourse(course);
        progress.setResource(resource);
        progress.setCompleted(true);

        progressRepository.save(progress);

        return ResponseEntity.ok("Resource marked as completed");
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ProgressDTO>> getProgressForCourse(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User student) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<Resource> resources = course.getResources();
        List<StudentProgress> progressList = progressRepository.findByStudentAndCourse(student, course);

        Map<Long, Boolean> progressMap = progressList.stream()
                .collect(Collectors.toMap(progress -> progress.getResource().getId(), StudentProgress::isCompleted));

        List<ProgressDTO> progressDTOList = resources.stream().map(resource -> {
            boolean completed = progressMap.getOrDefault(resource.getId(), false);
            ProgressDTO.ResourceDTO resourceDTO = new ProgressDTO.ResourceDTO(
                    resource.getId(),
                    resource.getTitle(),
                    resource.getDescription(),
                    resource.getFilePath()
            );
            return new ProgressDTO(resourceDTO, completed);
        }).toList();

        return ResponseEntity.ok(progressDTOList);
    }

    @GetMapping("/all")
    public ResponseEntity<List<StudentProgress>> getAllProgress(@AuthenticationPrincipal User student) {
        List<StudentProgress> progress = progressRepository.findByStudent(student);
        return ResponseEntity.ok(progress);
    }
}
