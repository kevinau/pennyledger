package org.pennyledger.form.value.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

public class FieldWrapper extends ObjectWrapper implements IFieldWrapper {

  private static final List<IObjectWrapper> noChildren = Collections.emptyList();

  private final String name;
  private final Class<?> type;
  
  protected FieldWrapper(IContainerReference container, String name, Class<?> type) {
    super(container);
    this.name = name;
    this.type = type;
  }

  @Override
  public boolean isField () {
    return true;
  }

  @Override
  public void walkObjectWrappers (IObjectVisitable x) {
    x.visit(this);
  }

  @Override
  public String toString() {
    return "FieldWrapper(" + name + "," + type.getSimpleName() + ")";
  }
  
  @Override
  public List<IObjectWrapper> getChildren() {
    return noChildren;
  }

  @Override
  public void walkFieldWrappers(IFieldVisitable x) {
    x.visit(this);
  }

}
