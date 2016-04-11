package org.pennyledger.form.plan.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.type.IType;

public class FieldPlan extends ObjectPlan implements IFieldPlan {

  private final IType<?> type;
  private final Field field;
  private final boolean nullable;
  //private final Field lastEntryField;
  //private final Object staticDefaultValue;
  
  
  public FieldPlan (IObjectPlan parent, String name, IType<?> type, Field field, EntryMode entryMode, boolean nullable) {
    super (parent, name, entryMode);
    if (type == null) { 
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    this.field = field;
    this.nullable = nullable;
    //this.lastEntryField = lastEntryField;
    //this.staticDefaultValue = staticDefaultValue;
  }
  

  @Override
  public IType<?> getType () {
    return type;
  }
  
  
  @Override
  public boolean isNullable () {
    return nullable;
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
    System.out.println("FieldPlan("  + type + ",nullable=" + nullable + "," + super.toString() + ")");
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


//  @Override
//  public IObjectModel buildModel(IForm<?> form, IObjectModel parent, IContainerReference container) {
//    return new FieldModel(form, parent, container, this);
//  }
//
//  @Override
//  public Object newValue () {
//    return type.newValue();
//  }


  @Override
  public <A extends Annotation> A getAnnotation(Class<A> klass) {
    return field.getAnnotation(klass);
  }


  @Override
  public void accumulateFieldPlans(List<IFieldPlan> fieldPlans) {
    fieldPlans.add(this);
  }

}
