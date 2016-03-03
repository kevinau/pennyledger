package org.pennyledger.form.factory;

import java.util.EventListener;

import org.pennyledger.form.value.IObjectWrapper;

public interface IObjectEvent<X extends EventListener> {
  
  public void eventFired (IObjectWrapper model, X listener);

}
