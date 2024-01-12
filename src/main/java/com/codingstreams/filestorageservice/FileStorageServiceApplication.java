package com.codingstreams.filestorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class FileStorageServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FileStorageServiceApplication.class, args);
  }

}
