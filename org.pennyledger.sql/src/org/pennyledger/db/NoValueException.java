package org.pennyledger.db;


public class NoValueException extends RuntimeException {

  private static final long serialVersionUID = 1L;


  public NoValueException () {
  }
  
  
  public NoValueException (String message) {
    super (message);
  }
  
}
