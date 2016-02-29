package org.pennyledger.form.plan.impl;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.PlanKind;

public abstract class ObjectPlan implements IObjectPlan {

  private final IObjectPlan parent;
  
  private final String name;
  
  private String staticLabel = "";
  
  private EntryMode staticMode = EntryMode.UNSPECIFIED;
  
  
  protected static String entityName (Class<?> entityClass) {
    String klassName = entityClass.getSimpleName();
    return Character.toLowerCase(klassName.charAt(0)) + klassName.substring(1);
  }
  
  
  protected static EntryMode entityEntryMode (Class<?> entityClass) {
    EntryMode entryMode = EntryMode.UNSPECIFIED;
    Mode modeAnn = entityClass.getAnnotation(Mode.class);
    if (modeAnn != null) {
      entryMode = modeAnn.value();
    }
    return entryMode;
  }
  
  
  @Override
  public boolean isOptional () {
    return false;
  }
  

  public ObjectPlan (IObjectPlan parent, String name, EntryMode entryMode) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    this.parent = parent;
    this.name = name;
    this.staticMode = entryMode;
  }
  
  
  @Override
  public IObjectPlan getParent() {
    return parent;
  }
  
  
  @Override
  public String getName () {
    return name;
  }


  @Override
  public String getDeclaredLabel () {
    return staticLabel;
  }
  

  @Override
  public EntryMode getDeclaredMode () {
    return staticMode;
  }
 
  
  @Override 
  public void dump () {
    dump (0);
  }
  
  
  @Override
  public abstract void dump (int level);


  @Override
  public abstract PlanKind kind();
  
}
