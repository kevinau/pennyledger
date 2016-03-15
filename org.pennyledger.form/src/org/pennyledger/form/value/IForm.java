package org.pennyledger.form.value;

import java.util.List;

import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.util.UserEntryException;

public interface IForm<T> {

//  public void setValue(T value);
//  
//  public T getValue();

  public List<IObjectModel> getObjectModels();

  public IObjectModel getObjectModel(String pathExpr);
  
  public IObjectPlan getPlan();

  public List<IObjectModel> getObjectModels(String pathExpr);

  public void walkObjectModels(IObjectVisitable x);

  public void walkObjectModels(String path, IObjectVisitable x);

  public List<IFieldModel> getFieldModels();

  public IFieldModel getFieldModel(String pathExpr);

  public List<IFieldModel> getFieldModels(String pathExpr);

  public void walkFieldModels(IFieldVisitable x);

  public void walkFieldModels(String path, IFieldVisitable x);

  public void fireValueEqualityChange(IFieldModel source);
  
  public void fireSourceEqualityChange(IFieldModel source, boolean isDataTrigger);

  public void fireComparisonBasisChange(IFieldModel source);
  
  public void fireEffectiveModeChange (IObjectModel source);

  public void fireErrorCleared(IObjectModel source);
  
  public void fireErrorNoted(IObjectModel source, UserEntryException ex);
  
  public void fireSourceChange(IFieldModel source);

  public void fireModelAdded(IContainerModel parent, IObjectModel model);

  public void fireModelRemoved(IContainerModel parent, IObjectModel model);

  public void fireModelReplaced(IContainerModel parent, IObjectModel removedModel, IObjectModel addedModel);

  public void fireValueChange(IFieldModel source);

  public void addEffectiveModeListener(String pathExpr, EffectiveModeListener x);

  public void addErrorListener(String pathExpr, ErrorListener x);

  public void addFieldEventListener(String pathExpr, FieldEventListener x);

  public void addModelChangeListener(ModelChangeListener x);

  public void removeEffectiveModeListener(String pathExpr, EffectiveModeListener x);

  public void removeErrorListener(String pathExpr, ErrorListener x);

  public void removeFieldEventListener(String pathExpr, FieldEventListener x);

  public void removeModelChangeListener(ModelChangeListener x);

  public void dump();

}
