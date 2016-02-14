package org.pennyledger.form.model;

import java.util.Collection;
import java.util.EventListener;

import org.pennyledger.form.EntryMode;
import org.pennyledger.util.UserEntryException;


public interface IObjectModel extends IBaseModel {

  public IFormModel<?> getOwnerForm();
  
  public IContainerModel getParent();
  
  /**
   * Add an EntryModeListener.
   */
  public void addEffectiveModeListener(EffectiveModeListener x);
  

  public void addFieldEventListener(FieldEventListener x);
  
  
  public void detach();
  
  
  public void dump (int level);
  
  
  //public void fireFieldEventsOnListener (FieldEventListener x, boolean isSourceTrigger);
  
  
  //public void fireModeEventsOnListener (EntryModeListener x);
  

  @Override
  public Collection<? extends IObjectModel> getChildren();

  
  @Override
  public EffectiveMode getEffectiveMode ();
  
  
  public String getPathName ();


  public Object getUnderlyingValue();


  public Object getValue();


  public boolean isArrayModel ();


  public boolean isContainerModel ();

  /**
   * Remove an EntryModeListener.
   */
  public void removeEffectiveModeListener(EffectiveModeListener x);
  
  
  public void removeFieldEventListener(FieldEventListener x);
  
  
  public void resetToInitial();
  

  public void setMode (EntryMode mode);

  
  public void setUnderlyingValue (Object value);

  
  public void setValue (Object value);
  
  
  public void setEventsActive (boolean fireEvents);

  
  public void fireEffectiveModeChange(IObjectModel eventSource);

  public void fireComparisonBasisChange (IFieldModel eventSource);
  
  public void fireCompareEqualityChange (IFieldModel eventSource);
  
  public void fireCompareShowingChange (IFieldModel eventSource, boolean isSourceTrigger);
  
  public void fireValueChange (IFieldModel eventSource, EventListener self);
  
  public void fireSourceChange (IFieldModel eventSource, EventListener self);
  
  public void fireErrorNoted (IFieldModel eventSource, UserEntryException ex);

  public void fireErrorCleared (IFieldModel eventSource);
  
  public void fireInitialEntryModes (EffectiveModeListener x);
  
  public void fireInitialFieldEvents (FieldEventListener x, boolean isSourceTrigger);
  
}
