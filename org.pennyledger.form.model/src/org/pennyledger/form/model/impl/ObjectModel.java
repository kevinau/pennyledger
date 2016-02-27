package org.pennyledger.form.model.impl;

import java.util.Collections;
import java.util.List;

import org.pennyledger.form.model.IForm;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.reflect.IContainerReference;

public class ObjectModel<T> implements IObjectModel<T> {

  private final IForm<?> form;
  private final IContainerReference containerRef;
  
  public ObjectModel (IForm<?> form, IContainerReference containerRef) {
    this.form = form;
    this.containerRef = containerRef;
  }
  
  private static final List<IObjectModel<?>> noChildren = Collections.emptyList();
  
  @Override
  public List<IObjectModel<?>> getChildren () {
    return noChildren;
  }

  @Override
  public T getValue() {
    return containerRef.getValue();
  }

  @Override
  public void setValue(T value) {
    containerRef.setValue(value);
  }

}
