package com.codingstreams.filestorageservice;

import com.codingstreams.filestorageservice.model.FileMetadata;
import org.springframework.data.repository.CrudRepository;

public interface FileMetadataRepo extends CrudRepository<FileMetadata, String> {
}
