package com.codingstreams.filestorageservice.service;

import com.codingstreams.filestorageservice.FileMetadataRepo;
import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import com.codingstreams.filestorageservice.exception.FileNotFoundException;
import com.codingstreams.filestorageservice.exception.UnableToFetchFileException;
import com.codingstreams.filestorageservice.model.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

  @Override
  public FileUploadResponse uploadFile(MultipartFile file) {
    try {
      // Check upload folder exists or not
      if (!Files.exists(Path.of(uploadPath))) {
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
      fileMetadataRepo.save(fileMetadata);

      // Save file
      // ./uploads/uploadFilename.extension
      var targetPath = uploadPath + "/" + uploadFilename;

      // Copy the file to target location
      Files.copy(file.getInputStream(), Path.of(targetPath), StandardCopyOption.REPLACE_EXISTING);

      // return file upload response
      return FileUploadResponse.builder()
          .filename(uploadFilename)
          .downloadUrl("/api/files/download/" + uploadFilename)
          .build();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ByteArrayResource downloadFile(String filename) {
    log.info("[downloadFile] - START");
    // Fetch file meta-data by filename
    var fileMetadata = fileMetadataRepo.findByUploadFilename(filename)
        .orElseThrow(FileNotFoundException::new);

    // Create resource path
    var resourcePath = uploadPath + "/" + fileMetadata.getUploadFilename();

    // Fetch file data
    try (var fileInputStream = new FileInputStream(resourcePath)) {
      log.info("[downloadFile] - END");
      return new ByteArrayResource(fileInputStream.readAllBytes());
    } catch (IOException e) {
      log.info("[downloadFile] - ERROR - {}", e.getMessage());
      throw new UnableToFetchFileException();
    }
  }
}
