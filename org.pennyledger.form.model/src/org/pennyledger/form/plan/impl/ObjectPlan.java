package org.pennyledger.form.plan.impl;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IObjectPlan;

public abstract class ObjectPlan implements IObjectPlan {

  private final Object parentRef;
  
  private final String pathName;
  
  private String staticLabel;
  
  private EntryMode staticMode = EntryMode.UNSPECIFIED;
  

  //@Deprecated
  public ObjectPlan (Object parentRef, String pathName, EntryMode entryMode) {
    if (pathName == null) {
      throw new IllegalArgumentException("Path name cannot be null");
    }
    this.parentRef = parentRef;
    this.pathName = pathName;
    this.staticMode = entryMode;
  }
  
  
  public ObjectPlan (String pathName, EntryMode entryMode) {
    if (pathName == null) {
      throw new IllegalArgumentException("Path name cannot be null");
    }
    this.parentRef = null;
    this.pathName = pathName;
    this.staticMode = entryMode;
  }
  
  
  @Override
  public Object getParentRef () {
    return parentRef;
  }
  
  
  @Override
  public String getPathName () {
    return pathName;
  }

  
  @Override
  public void setStaticLabel (String staticLabel) {
    this.staticLabel = staticLabel;
  }
  
  
  @Override
  public void setStaticMode (EntryMode staticMode) {
    this.staticMode = staticMode;
  }
  
  
  @Override
  public String getStaticLabel () {
    return staticLabel;
  }
  
  @Override
  public EntryMode getStaticMode () {
    return staticMode;
  }
 
  
//  public Object getValue (Object instance) {
//    try {
//      field.setAccessible(true);
//      return field.get(instance);
//    } catch (IllegalArgumentException ex) {
//      throw new RuntimeException(ex);
//    } catch (IllegalAccessException ex) {
//      throw new RuntimeException(ex);
//    }
//  }
  

  @Override 
  public void dump () {
    dump (0);
  }
  
  
  protected abstract void dump (int level);
  
}
