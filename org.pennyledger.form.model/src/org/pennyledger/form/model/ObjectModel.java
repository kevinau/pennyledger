package org.pennyledger.form.model;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.util.UniformEventListenerList;
import org.pennyledger.util.UserEntryException;
import org.jaxen.JaxenException;

public abstract class ObjectModel implements IObjectModel {

  private IFormModel<?> ownerForm;
  private final Object parentRef;
  //private final IObjectPlan objectPlan;

  protected IContainerModel parentModel;
  private boolean fireEvents = false;
  
  /**
   * The initial entry mode for this node.
   */
  private EntryMode staticMode;
  
  /**
   * The current mode of this model.  This will either be a value set by a ModeFor method, or the annotation
   * mode for the model, or <code>UNSPECIFIED</code> if neither is set.
   */
  private EntryMode objectMode;
  
  
  /**
   * The effective mode of this model, taking into account parent modes and the current mode.
   */
  private EffectiveMode effectiveMode;
  
  
  private final UniformEventListenerList effectiveModeListenerList = new UniformEventListenerList();
  private final UniformEventListenerList fieldEventListenerList = new UniformEventListenerList();

  
  public ObjectModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, IObjectPlan objectPlan) {
    if (ownerForm == null) {
      throw new IllegalArgumentException("Owner form must not be null");
    }
    this.ownerForm = ownerForm;
    this.parentModel = parentModel;
    this.parentRef = parentRef;
    //this.objectPlan = objectPlan;
    
    this.staticMode = objectPlan.getStaticMode();
    this.objectMode = staticMode;
    this.effectiveMode = getEffectiveMode();
  }
  

//  public ObjectModel(IBaseModel parentModel, Object parentRef, String pathName, EntryMode initialMode) {
//    if (parentModel == null) {
//      throw new IllegalArgumentException("Parent model must not be null");
//    }
//    this.parentModel = parentModel;
//    this.parentRef = parentRef;
//    this.pathName = pathName;
//    this.initialMode = initialMode;
//    this.objectMode = initialMode;
//    this.effectiveMode = calcEffectiveMode(this);
//  }
  
  
  protected boolean isEventsActive() {
    return fireEvents;
  }
  

  @Override
  public boolean isFieldModel() {
    return false;
  }
  
  
  @Override
  public boolean isContainerModel() {
    return false;
  }
  
  
  @Override
  public boolean isArrayModel() {
    return false;
  }
  
  
  @Override
  public String getPathName () {
    if (parentModel == null) {
      return "form";
    } else {
      return parentModel.getKey(this).toString();
    }
  }
  
  
  @Override
  public EntryMode getEntryMode () {
    return objectMode;
  }

  /**
   * Set the initial entry mode.  This does not fire a ModeChanged event.
   * 
   * @param mode
   */
  public void setStaticMode (EntryMode staticMode) {
    this.staticMode = staticMode;
  }
  
  
  @Override
  public EffectiveMode getEffectiveMode () {
    if (effectiveMode == null) {
      effectiveMode = calcEffectiveMode();
    }
    return effectiveMode;
  }
  
  
  private EffectiveMode calcEffectiveMode() {
    EffectiveMode parentMode;
    if (parentModel == null) {
      parentMode = ownerForm.getEffectiveMode();
    } else {
      parentMode = parentModel.getEffectiveMode();
    }
    return parentMode.getEffective(objectMode);
  }

  
  @Override
  public void setMode (EntryMode entryMode) {
    // Set the current mode
    this.objectMode = entryMode;
    
    // Now see if the effective mode has changed, and fire events if it has
    EffectiveMode newEffectiveMode = calcEffectiveMode();
    setEffectiveMode (newEffectiveMode);
  }
  
  
  private void setEffectiveMode (EffectiveMode effectiveMode) {
    if (this.effectiveMode != effectiveMode) {
      this.effectiveMode = effectiveMode;
      if (fireEvents) {
        fireEffectiveModeChange(this);
      }
      // A change in the inherited mode here, may change the inherited modes
      // of any members
      for (IObjectModel member : getChildren()) {
        ((ObjectModel)member).fixupEffectiveMode(effectiveMode);
      }
    }
  }
  
  
  private void fixupEffectiveMode (EffectiveMode parentMode) {
    EffectiveMode newEffectiveMode = parentMode.getEffective(objectMode);
    if (this.effectiveMode != newEffectiveMode) {
      this.effectiveMode = newEffectiveMode;
      if (fireEvents) {
        fireEffectiveModeChange(this);
      }

      // A change in the inherited mode here, may change the inherited modes
      // of any members
      for (IObjectModel member : getChildren()) {
        ((ObjectModel)member).fixupEffectiveMode(newEffectiveMode);
      }
    }
  }
  
  
  @Override
  public void addEffectiveModeListener (EffectiveModeListener x) {
    effectiveModeListenerList.add(x);
    // Fire all applicable events
    if (fireEvents) {
      x.modeChange(this);
    }
  }

  
  @Override
  public void removeEffectiveModeListener (EffectiveModeListener x) {
    effectiveModeListenerList.remove(x);
  }
  

  @Override
  public void addFieldEventListener (FieldEventListener x) {
    fieldEventListenerList.add(x);
    if (fireEvents) {
      fireInitialFieldEvents(x, false);
    }
  }

  
  @Override
  public void removeFieldEventListener (FieldEventListener x) {
    fieldEventListenerList.remove(x);
  }
  

  @Override
  public void fireInitialEntryModes (EffectiveModeListener x) {
    x.modeChange(this);
    for (IObjectModel childModel : getChildren()) {
      childModel.fireInitialEntryModes(x);
    }
  }
  

  @Override
  public void setEventsActive (boolean fireEvents) {
    this.fireEvents = fireEvents;
    if (fireEvents) {
      fireEffectiveModeChange(this);
    }
  }
  
  
  @Override
  public void fireEffectiveModeChange (IObjectModel eventSource) {
    if (fireEvents) {
      for (EventListener x : effectiveModeListenerList.listeners()) {
        ((EffectiveModeListener)x).modeChange(eventSource);
      }
      if (parentModel != null) {
        parentModel.fireEffectiveModeChange (eventSource);
      }
    }
  }

  
  @Override
  public void fireCompareEqualityChange (IFieldModel eventSource) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        ((FieldEventListener)x).compareEqualityChange(eventSource);
      }
      if (parentModel != null) {
        parentModel.fireCompareEqualityChange(eventSource);
      }
    }
  }

  
  @Override
  public void fireCompareShowingChange (IFieldModel eventSource, boolean isSourceTrigger) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        ((FieldEventListener)x).compareShowingChange(eventSource, isSourceTrigger);
      }
      if (parentModel != null) {
        parentModel.fireCompareShowingChange(eventSource, isSourceTrigger);
      }
    }
  }

  
  @Override
  public void fireValueChange (IFieldModel eventSource, EventListener self) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        if (x != self) {
          ((FieldEventListener)x).valueChange(eventSource);
        }
      }
      if (parentModel != null) {
        parentModel.fireValueChange(eventSource, self);
      }
    }
  }

  
  @Override
  public void fireSourceChange (IFieldModel eventSource, EventListener self) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        if (x != self) {
          ((FieldEventListener)x).sourceChange(eventSource);
        }
      }
      if (parentModel != null) {
        parentModel.fireSourceChange(eventSource, self);
      }
    }
  }

  
  @Override
  public void fireComparisonBasisChange (IFieldModel eventSource) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        ((FieldEventListener)x).comparisonBasisChange(eventSource);
      }
      if (parentModel != null) {
        parentModel.fireComparisonBasisChange(eventSource);
      }
    }
  }

  
  @Override
  public void fireErrorCleared (IFieldModel eventSource) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        ((FieldEventListener)x).errorCleared(eventSource);
      }
      if (parentModel != null) {
        parentModel.fireErrorCleared(eventSource);
      }
    }
  }


  @Override
  public void fireErrorNoted (IFieldModel eventSource, UserEntryException ex) {
    if (fireEvents) {
      for (EventListener x : fieldEventListenerList.listeners()) {
        ((FieldEventListener)x).errorNoted(eventSource, ex);
      }
      if (parentModel != null) {
        parentModel.fireErrorNoted(eventSource, ex);
      }
    }
  }

  
  @Override
  public void detach () {
    this.parentModel = null;
  }
  
  
  @Override 
  public Object getUnderlyingValue () {
    if (parentModel == null) {
      throw new RuntimeException("Model has been detached from parent");
    }
    return parentModel.getUnderlyingValue(parentRef);
  }
  
  
  @Override 
  public void setUnderlyingValue (Object value) {
    if (parentModel != null) {
      parentModel.setUnderlyingValue(parentRef, value);
    }
  }
  
  
  @Override
  public IContainerModel getParent () {
    return parentModel;
  }
  
  
  @Override
  public IFormModel<?> getOwnerForm () {
    return ownerForm;
  }
  
  
  @Override
  public List<IObjectModel> selectObjectModels(String xpathExpr) {
    try {
      FormXPath xpath = new FormXPath(xpathExpr);
      @SuppressWarnings("unchecked")
      List<IObjectModel> list = (List<IObjectModel>)xpath.evaluate(this);
      return list;
    } catch (JaxenException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public List<IFieldModel> selectFieldModels(String xpathExpr) {
    try {
      List<IFieldModel> fieldList = new ArrayList<IFieldModel>();
      
      FormXPath xpath = new FormXPath(xpathExpr);
      @SuppressWarnings("unchecked")
      List<IObjectModel> objList = (List<IObjectModel>)xpath.evaluate(this);
      for (IObjectModel objModel : objList) {
        if (objModel.isFieldModel()) {
          fieldList.add((IFieldModel)objModel);
        }
      }
      return fieldList;
    } catch (JaxenException ex) {
      throw new RuntimeException(ex);
    }
  }


  protected void accumulateFields (List<IFieldModel> list) {
    for (IObjectModel objModel : getChildren()) {
      ((ObjectModel)objModel).accumulateFields (list);
    }
  }
  
  
  @Override
  public List<IFieldModel> selectAllFieldModels() {
    List<IFieldModel> fieldList = new ArrayList<IFieldModel>();
    accumulateFields (fieldList);
    return fieldList;
  }


  @Override
  public IFieldModel selectFieldModel(String xpathExpr) {
    try {
      FormXPath xpath = new FormXPath(xpathExpr);
      List<?> list = (List<?>)xpath.evaluate(this);
      switch (list.size()) {
      case 0 :
        throw new RuntimeException("No object models found using '" + xpathExpr + "'");
      case 1 :
        Object model = list.get(0);
        if (model instanceof IFieldModel) {
          return (IFieldModel)model;
        } else {
          throw new RuntimeException("'" + xpathExpr + "' does not identify a field model");
        }
      default :
        throw new RuntimeException(list.size() + " object models found using '" + xpathExpr + "'");
      }
    } catch (JaxenException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  @SuppressWarnings("unchecked")
  @Override
  public <E extends IObjectModel> E selectObjectModel(String xpathExpr) {
    try {
      FormXPath xpath = new FormXPath(xpathExpr);
      List<?> list = (List<?>)xpath.evaluate(this);
      switch (list.size()) {
      case 0 :
        throw new RuntimeException("No object models found using '" + xpathExpr + "'");
      case 1 :
        IObjectModel model = (IObjectModel)list.get(0);
        return (E)model;
      default :
        throw new RuntimeException(list.size() + " object models found using '" + xpathExpr + "'");
      }
    } catch (JaxenException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  @Override
  public void dump () {
    dump (0);
  }

  
  @Override
  public String toString () {
    StringBuilder buffer = new StringBuilder();
    buffer.append("ObjectModel[");
    buffer.append("name=" + getPathName());
    buffer.append("]");
    return buffer.toString();
  }
  
  
  @Override
  public void resetToInitial () {
    setMode(staticMode);
  }
  
  
  @Override
  public boolean walkFields (IFieldVisitable x) {
    for (IObjectModel model : getChildren()) {
      boolean result = model.walkFields(x);
      if (result == false) {
        return false;
      }
    }
    return true;
  }

}
