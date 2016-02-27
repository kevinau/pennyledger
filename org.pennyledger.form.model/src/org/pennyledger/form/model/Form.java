package org.pennyledger.form.model;

import java.util.List;

import org.pennyledger.form.reflect.FormContainerReference;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;
import org.pennyledger.form.value.impl.ObjectWrapper;

public class Form<T> implements IForm<T> {

  private final Class<?> rootClass;
  private final IContainerReference container;
  
  private IObjectWrapper rootWrapper;
  
  public Form (Class<?> formClass) {
    this.rootClass = formClass;
    container = new FormContainerReference();
  }
  
  @Override
  public T getValue() {
    if (rootWrapper == null) {
      return null;
    } else {
      return rootWrapper.getValue();
    }
  }
  
  @Override
  public void setValue(Object value) {
    if (rootWrapper == null) {
      rootWrapper = ObjectWrapper.wrapValue(container, "form", rootClass, value);
    } else {
      rootWrapper.setValue(value);
    }
  }

  @Override
  public List<IObjectWrapper> getObjectWrappers() {
    return rootWrapper.getObjectWrappers();
  }

  @Override
  public IObjectWrapper getObjectWrapper(String pathExpr) {
    return rootWrapper.getObjectWrapper(pathExpr);
  }

  @Override
  public List<IObjectWrapper> getObjectWrappers(String pathExpr) {
    return rootWrapper.getObjectWrappers(pathExpr);
  }

  @Override
  public void walkObjectWrappers(IObjectVisitable x) {
    rootWrapper.walkObjectWrappers(x);
    
  }

  @Override
  public void walkObjectWrappers(String path, IObjectVisitable x) {
    rootWrapper.walkObjectWrappers(path, x);
  }

  @Override
  public List<IFieldWrapper> getFieldWrappers() {
    return rootWrapper.getFieldWrappers();
  }

  @Override
  public IFieldWrapper getFieldWrapper(String pathExpr) {
    return rootWrapper.getFieldWrapper(pathExpr);
  }

  @Override
  public List<IFieldWrapper> getFieldWrappers(String pathExpr) {
    return rootWrapper.getFieldWrappers(pathExpr);
  }

  @Override
  public void walkFieldWrappers(IFieldVisitable x) {
    rootWrapper.walkFieldWrappers(x);
  }

  @Override
  public void walkFieldWrappers(String path, IFieldVisitable x) {
    rootWrapper.walkFieldWrappers(path, x);
  }

}
