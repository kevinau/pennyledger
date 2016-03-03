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

  public IFieldModel getFieldModel(String pathExpr);

  public List<IFieldModel> getFieldModels();
  
  public List<IFieldModel> getFieldModels(String pathExpr);

  public IObjectModel getObjectModel(String pathExpr);
  
  public List<IObjectModel> getObjectModels();

  public List<IObjectModel> getObjectModels(String pathExpr);

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

  public void walkFieldModels(IFieldVisitable x);
  
  public void walkFieldModels(String path, IFieldVisitable x);
  
  public void walkObjectModels(IObjectVisitable x);

  public void walkObjectModels(String path, IObjectVisitable x);

}
