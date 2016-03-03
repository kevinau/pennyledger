package org.pennyledger.form.factory;

import java.util.EventListener;

import org.pennyledger.form.value.IFieldWrapper;

public interface IFieldEvent<X extends EventListener> {
  
  public void eventFired (IFieldWrapper model, X listener);

}
