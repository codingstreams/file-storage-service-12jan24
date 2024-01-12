package com.codingstreams.filestorageservice.service;

import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  FileUploadResponse uploadFile(MultipartFile file);

  ByteArrayResource downloadFile(String filename);
}
