package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;

public class SumInteger implements IAccumulation {

  private final Field field;
  private int total = 0;
  
  public SumInteger (Field field) {
    this.field = field;
  }
  
  @Override
  public void accumulate (Object data) {
    int value;
    try {
      field.setAccessible(true);
      value = (Integer)field.get(data);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    total += value;
  }

  @Override
  public Integer get() {
    return total;
  }
  
}
