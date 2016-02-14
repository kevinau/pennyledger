package org.pennyledger.form.plan.impl;

import java.lang.reflect.Field;

import javax.persistence.Basic;
import javax.persistence.Column;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.FormField;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.model.FieldModel;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.ObjectKind;

public class FieldPlan extends ObjectPlan implements IFieldPlan {

  private final IType<?> type;
  private final boolean nullable;
  private final Field lastEntryField;
  private final Object staticDefaultValue;
  
  
  public FieldPlan (Object parentRef, Field field, IType<?> type, EntryMode entryMode, Field lastEntryField, Object staticDefaultValue) {
    super (parentRef, field.getName(), entryMode);
    if (type == null) { 
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
    if (type.isPrimitive()) {
      // Primitives cannot be nullable
      nullable = false;
    } else {
      // If any annotation exists with a non-default value (ie a "false" value) 
      // the nullable is set to false.  Otherwise it stays at "true".
      boolean opt = true;
      FormField formFieldAnn = field.getAnnotation(FormField.class);
      if (formFieldAnn != null && formFieldAnn.nullable() == false) {
        opt = false;
      }
      Column columnAnn = field.getAnnotation(Column.class);
      if (columnAnn != null && columnAnn.nullable() == false) {
        opt = false;
      }
      Basic basicAnn = field.getAnnotation(Basic.class);
      if (basicAnn != null && basicAnn.optional() == false) {
        opt = false;
      }
      nullable = opt;
    }
    this.lastEntryField = lastEntryField;
    this.staticDefaultValue = staticDefaultValue;
  }
  

  @Override
  public IType<?> getType () {
    return type;
  }
  
  
  @Override
  public boolean isOptional () {
    return nullable;
  }
  
  
  public Field getLastEntryField () {
    return lastEntryField;
  }


  public Object getStaticDefaultValue () {
    return staticDefaultValue;
  }


  protected void dump (int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("Field: " + getStaticLabel() + " " + getStaticMode() + " (" + type + ")");
  }
  
  
  public Object newValue () {
    if (nullable) {
      return null;
    } else {
      return type.primalValue();
    }
  }


  @Override
  public IObjectModel createModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, Object instance) {
    return new FieldModel(ownerForm, parentModel, parentRef, pathName, this, instance);
  }


  @Override
  public String toString() {
    return "FieldPlan[type=" + type + "]";
  }
  
  
  @Override
  public boolean isSolitary() {
    return true;
  }


  @Override
  public ObjectKind kind() {
    return ObjectKind.FIELD;
  }

}
