package org.pennyledger.object.plan;

import org.pennyledger.object.UserEntryException;


public interface IMethodRunnable {

  public <T> void run (T instance) throws UserEntryException;
  
}
