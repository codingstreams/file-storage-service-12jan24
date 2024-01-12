package com.codingstreams.filestorageservice.exception;

public class UnableToFetchFileException extends RuntimeException {
  public UnableToFetchFileException() {
    super("Unable to fetch file");
  }
}
