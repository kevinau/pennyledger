package org.gyfor.reportdb;

import java.lang.reflect.Field;

import org.pennyledger.math.Decimal;

public class DecimalAve implements IAccumulationColumn {

  private final Field field;
  
  private int count = 0;
  private Decimal sum;
  
  public DecimalAve (Field field) {
    this.field = field;
  }
  
  @Override
  public void accumulate(Object source) {
    try {
      field.setAccessible(true);
      Decimal v = (Decimal)field.get(source);
      if (count == 0) {
        sum = v;
      } else {
        sum = sum.add(v);
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
      return Decimal.ZERO;
    } else {
      return sum.divide(count);
    }
  }

}
