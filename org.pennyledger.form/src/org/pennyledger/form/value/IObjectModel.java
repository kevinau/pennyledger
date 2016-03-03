package org.pennyledger.form.value;

import java.util.List;

import org.pennyledger.form.plan.IObjectPlan;

public interface IObjectModel {

  public void dispose();

  public default void dump() {
    dump(0);
  }

  public void dump(int level);

  public List<IObjectModel> getChildren();

  public IFieldModel getFieldWrapper(String pathExpr);

  public List<IFieldModel> getFieldWrappers();
  
  public List<IFieldModel> getFieldWrappers(String pathExpr);

  public IObjectModel getObjectWrapper(String pathExpr);
  
  public List<IObjectModel> getObjectWrappers();

  public List<IObjectModel> getObjectWrappers(String pathExpr);

  public IObjectModel getParent();

  public IObjectPlan getPlan();

  public <T> T getValue();

  public default void indent(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
  }

  public boolean isClass();

  public boolean isField();

  public boolean isInterface();

  public void setValue(Object value);

  public void syncCurrentValue();

  public void walkFieldWrappers(IFieldVisitable x);
  
  public void walkFieldWrappers(String path, IFieldVisitable x);
  
  public void walkObjectWrappers(IObjectVisitable x);

  public void walkObjectWrappers(String path, IObjectVisitable x);

}
