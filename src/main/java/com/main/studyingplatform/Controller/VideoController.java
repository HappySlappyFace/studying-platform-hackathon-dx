package com.main.studyingplatform.Controller;

import com.main.studyingplatform.Entities.Resource;
import com.main.studyingplatform.Entities.User;
import com.main.studyingplatform.Repository.ResourceRepository;
import com.main.studyingplatform.Service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ResourceRepository resourceRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file,
                                              @RequestParam("title") String title,
                                              @RequestParam("description") String description,
                                              @AuthenticationPrincipal User user) {
        try {
            // Save the file
            String fileName = fileStorageService.saveFile(file);

            // Create and save the resource
            Resource resource = new Resource();
            resource.setTitle(title);
            resource.setDescription(description);
            resource.setFilePath(fileName);
            resource.setUploadedBy(user);
            resourceRepository.save(resource);

            return ResponseEntity.ok("Video uploaded successfully and associated with teacher: " + user.getUsername());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video: " + e.getMessage());
        }
    }
}
