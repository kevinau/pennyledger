package org.pennyledger.form.model;

import java.util.List;

import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectWrapper;

public interface IForm<T> {

  public void setValue(T value);
  
  public T getValue();

  public List<IObjectWrapper> getObjectWrappers();

  public IObjectWrapper getObjectWrapper(String pathExpr);

  public List<IObjectWrapper> getObjectWrappers(String pathExpr);

  public void walkObjectWrappers(IObjectVisitable x);

  public void walkObjectWrappers(String path, IObjectVisitable x);

  public List<IFieldWrapper> getFieldWrappers();

  public IFieldWrapper getFieldWrapper(String pathExpr);

  public List<IFieldWrapper> getFieldWrappers(String pathExpr);

  public void walkFieldWrappers(IFieldVisitable x);

  public void walkFieldWrappers(String path, IFieldVisitable x);

}
