package org.pennyledger.form.value;

import java.util.List;

import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.util.UserEntryException;

public interface IForm<T> {

  public void setValue(T value);
  
  public T getValue();

  public List<IObjectWrapper> getObjectWrappers();

  public IObjectWrapper getObjectWrapper(String pathExpr);
  
  public IClassPlan<?> getPlan();

  public List<IObjectWrapper> getObjectWrappers(String pathExpr);

  public void walkObjectWrappers(IObjectVisitable x);

  public void walkObjectWrappers(String path, IObjectVisitable x);

  public List<IFieldWrapper> getFieldWrappers();

  public IFieldWrapper getFieldWrapper(String pathExpr);

  public List<IFieldWrapper> getFieldWrappers(String pathExpr);

  public void walkFieldWrappers(IFieldVisitable x);

  public void walkFieldWrappers(String path, IFieldVisitable x);

  public void fireValueEqualityChange(IFieldWrapper source);
  
  public void fireSourceEqualityChange(IFieldWrapper source, boolean isDataTrigger);

  public void fireComparisonBasisChange(IFieldWrapper source);
  
  public void fireEffectiveModeChange (IObjectWrapper source);

  public void fireErrorCleared(IObjectWrapper source);
  
  public void fireErrorNoted(IObjectWrapper source, UserEntryException ex);
  
  public void fireSourceChange(IFieldWrapper source);

  public void fireValueChange(IFieldWrapper source);

  public void addEffectiveModeListener(String pathExpr, EffectiveModeListener x);

  public void addErrorListener(String pathExpr, ErrorListener x);

  public void addFieldEventListener(String pathExpr, FieldEventListener x);

  public void removeEffectiveModeListener(String pathExpr, EffectiveModeListener x);

  public void removeErrorListener(String pathExpr, ErrorListener x);

  public void removeFieldEventListener(String pathExpr, FieldEventListener x);

  public void dump();

}
