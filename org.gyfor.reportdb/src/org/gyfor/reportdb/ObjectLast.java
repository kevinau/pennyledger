package org.gyfor.reportdb;

import java.lang.reflect.Field;

public class ObjectLast implements IAccumulationColumn {

  private final Field field;

  private Object last = null;
  
  
  public ObjectLast (Field field) {
    this.field = field;
  }
  
  
  @Override
  public void accumulate(Object source) {
    try {
      field.setAccessible(true);
      last = field.get(source);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  @Override
  public void reset() {
    last = null;
  }

  
  @Override
  public Object getValue() {
    return last;
  }

}
