package org.pennyledger.object.plan.impl;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.Mode;
import org.pennyledger.object.plan.IObjectPlan;
import org.pennyledger.object.plan.PlanKind;
import org.pennyledger.util.CamelCase;

public abstract class ObjectPlan implements IObjectPlan {

  private final IObjectPlan parent;
  
  private final String name;
  
  private String staticLabel = "";
  
  private EntryMode staticMode = EntryMode.UNSPECIFIED;
  
  
  protected static String entityName (Class<?> entityClass) {
    String klassName = entityClass.getSimpleName();
    return Character.toLowerCase(klassName.charAt(0)) + klassName.substring(1);
  }
  
  
  protected static String entityLabel (Class<?> entityClass) {
    String klassName = entityClass.getSimpleName();
    return CamelCase.toSentence(klassName);
  }
  
  
  protected static EntryMode entityEntryMode (Class<?> entityClass) {
    EntryMode entryMode = EntryMode.UNSPECIFIED;
    Mode modeAnn = entityClass.getAnnotation(Mode.class);
    if (modeAnn != null) {
      entryMode = modeAnn.value();
    }
    return entryMode;
  }
  
  
  public ObjectPlan (IObjectPlan parent, String name, String label, EntryMode entryMode) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    this.parent = parent;
    this.name = name;
    this.staticLabel = label;
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
  
  @Override
  public String toString () {
    return "ObjectPlan(" + name + ",'" + staticLabel + "'," + staticMode + ")";
  }

}
