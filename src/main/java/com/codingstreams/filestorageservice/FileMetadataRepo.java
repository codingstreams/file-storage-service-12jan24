package com.codingstreams.filestorageservice;

import com.codingstreams.filestorageservice.model.FileMetadata;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FileMetadataRepo extends CrudRepository<FileMetadata, String> {
  Optional<FileMetadata> findByUploadFilename(String filename);
}
