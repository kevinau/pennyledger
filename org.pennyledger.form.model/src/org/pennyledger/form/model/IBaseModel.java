package org.pennyledger.form.model;

import java.util.Collection;
import java.util.List;

import org.pennyledger.form.EntryMode;


public interface IBaseModel {

  public List<IObjectModel> selectObjectModels(String xpathExpr);

  public List<IFieldModel> selectFieldModels(String xpathExpr);

  public List<IFieldModel> selectAllFieldModels();
  
  public IFieldModel selectFieldModel(String xpathExpr);

  public <E extends IObjectModel> E selectObjectModel(String xpathExpr);

  public EntryMode getEntryMode();
  
  public EffectiveMode getEffectiveMode();

  public void dump();

  public Object getUnderlyingValue(Object parentRef);
  
  public void setUnderlyingValue(Object parentRef, Object value);
  
  public boolean walkFields (IFieldVisitable x);

  public Collection<? extends IObjectModel> getChildren();

//  public void fireComparisonBasisChange (IFieldModel model);
//  
//  public void fireCompareEqualityChange (IFieldModel model);
//  
//  public void fireCompareShowingChange (IFieldModel model, boolean isSourceTrigger);
//  
//  public void fireValueChange (IFieldModel model);
//
//  public void fireSourceChange (IFieldModel model);
//  
//  public void fireErrorNoted (IFieldModel model, UserEntryException ex);
//  
//  public void fireErrorCleared (IFieldModel model);
//  
//  public void setEventsActive (boolean fireEvents);
  
  public boolean isFieldModel ();

}
