package org.pennyledger.object.plan.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.pennyledger.object.EntryMode;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.plan.IObjectPlan;
import org.pennyledger.object.plan.PlanKind;
import org.pennyledger.object.type.IType;

public class FieldPlan<T> extends ObjectPlan implements IFieldPlan<T> {

  private final IType<T> type;
  private final Field field;
  private final boolean nullable;
  //private final Field lastEntryField;
  //private final Object staticDefaultValue;
  
  
  public FieldPlan (IObjectPlan parent, String name, String label, IType<T> type, Field field, EntryMode entryMode, boolean nullable) {
    super (parent, name, label, entryMode);
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
  public IType<T> getType () {
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
    return "FieldPlan[type=" + type + "," + super.toString() + "]";
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
  public void accumulateFieldPlans(List<IFieldPlan<?>> fieldPlans) {
    fieldPlans.add(this);
  }

}
