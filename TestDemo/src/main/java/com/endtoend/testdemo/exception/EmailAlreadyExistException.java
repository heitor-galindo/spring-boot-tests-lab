package com.endtoend.testdemo.exception;

public class EmailAlreadyExistException extends RuntimeException {
  public EmailAlreadyExistException() {
    super("Email Already Exists");
  }

  public EmailAlreadyExistException(String message) {
    super(message);
  }
}
