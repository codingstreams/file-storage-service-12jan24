package com.codingstreams.filestorageservice.exception;

public class FileNotFoundException extends RuntimeException {
  public FileNotFoundException() {
    super("File not found");
  }
}
