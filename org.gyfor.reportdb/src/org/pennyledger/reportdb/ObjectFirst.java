package org.pennyledger.reportdb;

import java.lang.reflect.Field;

public class ObjectFirst implements IAccumulationColumn {

  private final Field field;

  private int count = 0;
  private Object first;
  
  
  public ObjectFirst (Field field) {
    this.field = field;
  }
  
  
  @Override
  public void accumulate(Object source) {
    if (count == 0) {
      try {
        field.setAccessible(true);
        first = field.get(source);
      } catch (IllegalArgumentException | IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
    }
    count++;
  }

  
  @Override
  public void reset() {
    count = 0;
  }

  
  @Override
  public Object getValue() {
    return first;
  }

}
