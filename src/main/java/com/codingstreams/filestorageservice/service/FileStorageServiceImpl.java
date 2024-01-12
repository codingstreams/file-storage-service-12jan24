package com.codingstreams.filestorageservice.service;

import com.codingstreams.filestorageservice.FileMetadataRepo;
import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import com.codingstreams.filestorageservice.model.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
  @Value("${file.upload.path}")
  private String uploadPath;

  private final FileMetadataRepo fileMetadataRepo;
  private final Environment environment;

  @Override
  public FileUploadResponse uploadFile(MultipartFile file) {
    log.info("[uploadFile] - START");
    try {
      // Check upload folder exists or not
      if (!Files.exists(Path.of(uploadPath))) {
        log.info("Creating upload folder");
        // Create upload folder
        Files.createDirectories(Path.of(uploadPath));
      }

      // Original filename
      // image.png -> .png
      var originalFilename = file.getOriginalFilename();

      // File extension
      var fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

      // Upload file name
      var uploadFilename = UUID.randomUUID() + fileExtension;

      // Create meta-data object
      var fileMetadata = FileMetadata.builder()
          .originalFilename(originalFilename)
          .fileExtension(fileExtension)
          .uploadFilename(uploadFilename)
          .build();

      // Save file meta-data
      log.info("Started saving file meta-data");
      fileMetadataRepo.save(fileMetadata);
      log.info("End saving file meta-data");

      // Save file
      // ./uploads/uploadFilename.extension
      var targetPath = String.format("%s/%s", uploadPath, uploadFilename);

      // Copy the file to target location
      log.info("Started saving file");
      Files.copy(file.getInputStream(), Path.of(targetPath), StandardCopyOption.REPLACE_EXISTING);
      log.info("End saving file");

      // return file upload response
      log.info("[uploadFile] - END");
      return FileUploadResponse.builder()
          .filename(uploadFilename)
          .downloadUrl(String.format("/api/files/download/%s", uploadFilename))
          .build();

    } catch (IOException e) {
      log.info("[uploadFile] - ERROR {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] downloadFile(String filename) {
    log.info("[downloadFile] - START");
    // Find file meta-data by filename
    var fileMetadata = fileMetadataRepo.findByUploadFilename(filename)
        .orElseThrow(() -> new RuntimeException("File not found"));

    var resourcePath = String.format("%s/%s", uploadPath, fileMetadata.getUploadFilename());

    try (var fileInputStream = new FileInputStream(resourcePath)) {
      log.info("[downloadFile] - END");
      return fileInputStream.readAllBytes();
    } catch (IOException e) {
      log.info("[downloadFile] - ERROR - {}", e.getMessage());
      throw new RuntimeException();
    }
  }
}
