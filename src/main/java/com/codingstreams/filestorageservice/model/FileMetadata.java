package com.codingstreams.filestorageservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String originalFilename;
  private String uploadFilename;

  @CreationTimestamp
  private LocalDateTime creationTime;
  private String fileExtension;
}
