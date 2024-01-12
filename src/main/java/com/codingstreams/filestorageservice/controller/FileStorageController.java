package com.codingstreams.filestorageservice.controller;

import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import com.codingstreams.filestorageservice.exception.FileNotFoundException;
import com.codingstreams.filestorageservice.exception.UnableToFetchFileException;
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

    // Check for empty file
    if (Objects.isNull(file) || file.isEmpty()) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(
              FileUploadResponse.builder()
                  .errorMessage("Invalid file")
                  .build()
          );
    }

    log.info("Filename: {}", file.getOriginalFilename());
    log.info("Content Type: {}", file.getContentType());
    log.info("File Size: {}", file.getSize());

    try {
      var fileUploadResponse = fileStorageService.uploadFile(file);

      return ResponseEntity
          .status(HttpStatus.OK)
          .body(fileUploadResponse);
    } catch (Exception e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              FileUploadResponse.builder()
                  .errorMessage("Unable to upload file")
                  .build()
          );
    }
  }

  // Downloading the file
  @GetMapping("/download/{fileName}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable(name = "fileName") String filename) {
    try {
      var byteArrayResource = fileStorageService.downloadFile(filename);

      return ResponseEntity.ok()
          .headers(httpHeaders ->
              httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename))
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(byteArrayResource);
    } catch (FileNotFoundException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(null);
    } catch (UnableToFetchFileException e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(null);
    }
  }
}
