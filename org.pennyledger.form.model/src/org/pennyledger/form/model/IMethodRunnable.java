package org.pennyledger.form.model;

import org.pennyledger.util.UserEntryException;


public interface IMethodRunnable {

  public <T> void run (T instance) throws UserEntryException;
  
}
