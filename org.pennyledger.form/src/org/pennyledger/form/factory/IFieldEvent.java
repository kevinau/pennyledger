package org.pennyledger.form.factory;

import java.util.EventListener;

import org.pennyledger.form.value.IFieldModel;

public interface IFieldEvent<X extends EventListener> {
  
  public void eventFired (IFieldModel model, X listener);

}
