package org.pennyledger.form.value;

import java.util.List;

import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.util.UserEntryException;

public interface IForm<T> {

  public void setValue(T value);
  
  public T getValue();

  public List<IObjectModel> getObjectWrappers();

  public IObjectModel getObjectWrapper(String pathExpr);
  
  public IClassPlan<?> getPlan();

  public List<IObjectModel> getObjectWrappers(String pathExpr);

  public void walkObjectWrappers(IObjectVisitable x);

  public void walkObjectWrappers(String path, IObjectVisitable x);

  public List<IFieldModel> getFieldWrappers();

  public IFieldModel getFieldWrapper(String pathExpr);

  public List<IFieldModel> getFieldWrappers(String pathExpr);

  public void walkFieldWrappers(IFieldVisitable x);

  public void walkFieldWrappers(String path, IFieldVisitable x);

  public void fireValueEqualityChange(IFieldModel source);
  
  public void fireSourceEqualityChange(IFieldModel source, boolean isDataTrigger);

  public void fireComparisonBasisChange(IFieldModel source);
  
  public void fireEffectiveModeChange (IObjectModel source);

  public void fireErrorCleared(IObjectModel source);
  
  public void fireErrorNoted(IObjectModel source, UserEntryException ex);
  
  public void fireSourceChange(IFieldModel source);

  public void fireValueChange(IFieldModel source);

  public void addEffectiveModeListener(String pathExpr, EffectiveModeListener x);

  public void addErrorListener(String pathExpr, ErrorListener x);

  public void addFieldEventListener(String pathExpr, FieldEventListener x);

  public void removeEffectiveModeListener(String pathExpr, EffectiveModeListener x);

  public void removeErrorListener(String pathExpr, ErrorListener x);

  public void removeFieldEventListener(String pathExpr, FieldEventListener x);

  public void dump();

}
