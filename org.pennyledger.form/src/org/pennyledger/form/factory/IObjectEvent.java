package org.pennyledger.form.factory;

import java.util.EventListener;

import org.pennyledger.form.value.IObjectModel;

public interface IObjectEvent<X extends EventListener> {
  
  public void eventFired (IObjectModel model, X listener);

}
