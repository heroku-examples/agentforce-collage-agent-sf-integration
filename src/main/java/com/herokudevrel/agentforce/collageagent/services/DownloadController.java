package com.herokudevrel.agentforce.collageagent.services;

import io.awspring.cloud.s3.S3Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Collage API", description = "Downloads Collages")
@RestController
@RequiredArgsConstructor
public class DownloadController {

    private final StorageService storageService;

    @GetMapping("/download/{id}")
    @Operation(summary = "Download a collage", description = "Downloads a collage created with the /api/generate operation.")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws IOException {
        S3Resource s3Resource = storageService.retrieve(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(s3Resource);
    }
}
