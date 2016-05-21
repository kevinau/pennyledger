package org.gyfor.reportdb;

import java.lang.reflect.Field;

import org.pennyledger.math.Decimal;

public class DecimalSum implements IAccumulationColumn {

  private final Field field;
  
  private Decimal sum = Decimal.ZERO;
  
  
  public DecimalSum (Field field) {
    this.field = field;
  }

  
  @Override
  public void accumulate(Object source) {
    try {
      field.setAccessible(true);
      Decimal augend = (Decimal)field.get(source);
      sum = sum.add(augend);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void reset() {
    sum = Decimal.ZERO;
  }
  
  
  @Override
  public Object getValue() {
    return sum;
  }
}
