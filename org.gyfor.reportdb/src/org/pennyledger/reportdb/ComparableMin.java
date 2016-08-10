package org.pennyledger.reportdb;

import java.lang.reflect.Field;

public class ComparableMin implements IAccumulationColumn {

  private final Field field;
  
  private int count = 0;
  private Comparable<Object> min;
  
  public ComparableMin (Field field) {
    this.field = field;
  }
  
  
  @Override
  public void accumulate(Object source) {
    try {
      field.setAccessible(true);
      @SuppressWarnings("unchecked")
      Comparable<Object> comparable = (Comparable<Object>)field.get(source);
      if (count == 0) {
        min = comparable;
      } else if (min.compareTo(comparable) > 0) {
        min = comparable;
      }
      count++;
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void reset() {
    count = 0;
  }

  @Override
  public Object getValue() {
    if (count == 0) {
      return null;
    } else {
      return min;
    }
  }
  
  
}
