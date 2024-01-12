package com.codingstreams.filestorageservice.controller;

import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import com.codingstreams.filestorageservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@Slf4j
@RequiredArgsConstructor
public class FileStorageController {
  private final FileStorageService fileStorageService;

  // Uploading the file
  @PostMapping("/upload")
  public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam(name = "file") MultipartFile file) {
    return null;
  }

  // Downloading the file
  @GetMapping("/download/{fileName}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "fileName") String filename) {
    return null;
  }
}
