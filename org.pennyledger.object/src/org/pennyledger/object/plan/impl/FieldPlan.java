package org.pennyledger.object.plan.impl;

import java.lang.reflect.Field;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.Mode;
import org.pennyledger.object.plan.INodePlan;
import org.pennyledger.object.plan.PlanKind;
import org.pennyledger.util.CamelCase;

public abstract class FieldPlan implements INodePlan {

  private final INodePlan parent;
  
  private final String name;
  
  private final Field field;
  
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
  
  
  public FieldPlan (INodePlan parent, String name, Field field, String label, EntryMode entryMode) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (field == null) {
      throw new IllegalArgumentException("Field cannot be null");
    }
    this.parent = parent;
    this.name = name;
    this.field = field;
    this.staticLabel = label;
    this.staticMode = entryMode;
  }
  
  
  @Override
  public INodePlan getParent() {
    return parent;
  }
  
  
  @Override
  public String getName () {
    return name;
  }


  @Override
  public Field getField () {
    return field;
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
    return "FieldPlan(" + name + ",'" + staticLabel + "'," + staticMode + ")";
  }

}
