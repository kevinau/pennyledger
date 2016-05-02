package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;

import org.pennyledger.object.type.IType;


public class FieldValueSet implements IValueSet {

  private final Field[] fields;
  private final ReportColumn[] columns;

  private Object instance;


  public FieldValueSet(Field[] fields, ReportColumn[] columns) {
    this.fields = fields;
    this.columns = columns;
  }


  public void setInstance(Object instance) {
    this.instance = instance;
  }


  @Override
  public int size() {
    return fields.length;
  }
  
  
  @Override
  public Object get(int index) {
    try {
      return fields[index].get(instance);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public IType<?> typeOf(int index) {
    
    return types[index];
  }
  
}
