package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;

import org.pennyledger.math.Decimal;

public class WeightedAve implements IAccumulation {

  private final Field field1;
  private final Field field2;
  
  private Decimal sum1 = Decimal.ZERO;
  private Decimal sum2 = Decimal.ZERO;
  
  public WeightedAve (Field field1, Field field2) {
    this.field1 = field1;
    this.field2 = field2;
  }
  
  @Override
  public void accumulate (Object data) {
    Decimal v1;
    Decimal v2;
    try {
      field1.setAccessible(true);
      v1 = (Decimal)field1.get(data);
      field2.setAccessible(true);
      v2 = (Decimal)field2.get(data);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    sum1 = sum1.add(v1.multiply(v2));
    sum2 = sum2.add(v2);
  }

  
  @Override
  public Decimal get() {
    if (sum2.isZero()) {
      return null;
    } else {
      return sum1.divide(sum2, sum1.getScale());
    }
  }
  
}
