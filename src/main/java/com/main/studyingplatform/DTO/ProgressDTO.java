package com.main.studyingplatform.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressDTO {
    private ResourceDTO resource;
    private boolean completed;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResourceDTO {
        private Long id;
        private String title;
        private String description;
        private String filePath;
    }
}
