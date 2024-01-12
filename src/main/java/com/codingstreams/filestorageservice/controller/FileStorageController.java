package com.codingstreams.filestorageservice.controller;

import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import com.codingstreams.filestorageservice.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/files")
@Slf4j
@RequiredArgsConstructor
public class FileStorageController {
  private final FileStorageService fileStorageService;

  // Uploading the file
  @PostMapping("/upload")
  public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam(name = "file") MultipartFile file) {

    if (Objects.isNull(file) || file.isEmpty()) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              FileUploadResponse.builder()
                  .errorMessage("File is null")
                  .build()
          );
    }

    log.info("Filename: {}", file.getOriginalFilename());
    log.info("File Content Type: {}", file.getContentType());
    log.info("File Size: {}", file.getSize());

    try {
      var fileUploadResponse = fileStorageService.uploadFile(file);

      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(fileUploadResponse);
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              FileUploadResponse.builder()
                  .errorMessage("File not uploaded successfully!")
                  .build()
          );
    }
  }

  // Downloading the file
  @GetMapping("/download/{fileName}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "fileName") String filename) {
    byte[] bytes;
    try {
      bytes = fileStorageService.downloadFile(filename);

      var byteArrayResource = new ByteArrayResource(bytes);

      return ResponseEntity
          .status(HttpStatus.OK)
          .headers(httpHeaders ->
              httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", filename)))
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(byteArrayResource);
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .build();
    }
  }
}
