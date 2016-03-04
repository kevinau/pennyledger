package org.pennyledger.form.plan.impl;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.value.IObjectModel;
import org.pennyledger.form.value.impl.FieldModel;

public class FieldPlan extends ObjectPlan implements IFieldPlan {

  private final IType<?> type;
  private final boolean optional;
  //private final Field lastEntryField;
  //private final Object staticDefaultValue;
  
  
  public FieldPlan (IObjectPlan parent, String name, IType<?> type, EntryMode entryMode, boolean optional) {
    super (parent, name, entryMode);
    if (type == null) { 
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    this.optional = optional;
    //this.lastEntryField = lastEntryField;
    //this.staticDefaultValue = staticDefaultValue;
  }
  

  @Override
  public IType<?> getType () {
    return type;
  }
  
  
  @Override
  public boolean isOptional () {
    return optional;
  }
  
  
//  public Field getLastEntryField () {
//    return lastEntryField;
//  }


//  @Override
//  public Object getStaticDefaultValue () {
//    return staticDefaultValue;
//  }


  @Override
  public void dump (int level) {
    indent(level);
    System.out.println("FieldPlan("  + type + ",optional=" + optional + "," + super.toString() + ")");
  }
  
  
//  @SuppressWarnings("unchecked")
//  @Override
//  public <X> X newInstance () {
//    return (X)staticDefaultValue;
//  }

  @Override
  public String toString() {
    return "FieldPlan[type=" + type + "]";
  }
  
  
  @Override
  public PlanKind kind() {
    return PlanKind.FIELD;
  }


  @Override
  public IObjectModel buildModel(IObjectModel parent, IContainerReference container) {
    return new FieldModel(parent, container, this);
  }

  @Override
  public Object newValue () {
    return type.newValue();
  }

}
