package org.pennyledger.form.plan;

import org.pennyledger.util.UserEntryException;


public interface IMethodRunnable {

  public <T> void run (T instance) throws UserEntryException;
  
}
