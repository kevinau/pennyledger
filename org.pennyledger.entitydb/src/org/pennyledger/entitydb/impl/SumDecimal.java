package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;

import org.pennyledger.math.Decimal;

public class SumDecimal implements IAccumulation {

  private final Field field;
  private Decimal total = Decimal.ZERO;
  
  public SumDecimal (Field field) {
    this.field = field;
  }
  
  @Override
  public void accumulate (Object data) {
    Decimal value;
    try {
      field.setAccessible(true);
      value = (Decimal)field.get(data);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    total = total.add(value);
  }

  @Override
  public Decimal get() {
    return total;
  }
  
}
