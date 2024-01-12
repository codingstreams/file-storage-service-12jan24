package com.codingstreams.filestorageservice.service;

import com.codingstreams.filestorageservice.FileMetadataRepo;
import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
  @Value("${file.upload.path}")
  private String uploadPath;

  private final FileMetadataRepo fileMetadataRepo;

  @Override
  public FileUploadResponse uploadFile(MultipartFile file) {
    // Check upload folder exists or not

    // Create upload folder

    // Original filename
    // image.png -> .png

    // File extension

    // Upload file name

    // Create meta-data object

    // Save file meta-data

    // Save file
    // ./uploads/uploadFilename.extension

    // Copy the file to target location

    // return file upload response

    return null;
  }
}
