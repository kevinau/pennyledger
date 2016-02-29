package org.pennyledger.form.value;

import java.util.List;

public interface IObjectWrapper {

  public List<IObjectWrapper> getChildren();

  public List<IObjectWrapper> getObjectWrappers();

  public List<IFieldWrapper> getFieldWrappers();

  public <T> T getValue();

  public boolean isClass();

  public boolean isField();

  public IObjectWrapper getObjectWrapper(String pathExpr);

  public IFieldWrapper getFieldWrapper(String pathExpr);

  public List<IObjectWrapper> getObjectWrappers(String pathExpr);

  public List<IFieldWrapper> getFieldWrappers(String pathExpr);

  public void setValue(Object value);

  public void walkObjectWrappers(IObjectVisitable x);

  public void walkFieldWrappers(IFieldVisitable x);

  public void walkObjectWrappers(String path, IObjectVisitable x);

  public void walkFieldWrappers(String path, IFieldVisitable x);

}
