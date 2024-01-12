package com.codingstreams.filestorageservice.service;

import com.codingstreams.filestorageservice.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  FileUploadResponse uploadFile(MultipartFile file);
}
