package org.gyfor.reportdb;

import java.lang.reflect.Field;

import org.pennyledger.math.Decimal;

public class DecimalWeightedAve implements IAccumulationColumn {

  private final Field field1;
  private final Field field2;
  
  private Decimal productSum = Decimal.ZERO;
  private Decimal sum = Decimal.ZERO;
  
  
  public DecimalWeightedAve (Field field1, Field field2) {
    this.field1 = field1;
    this.field2 = field2;
  }
  
  
  @Override
  public void accumulate(Object source) {
    try {
      field1.setAccessible(true);
      Decimal v1 = (Decimal)field1.get(source);
      field2.setAccessible(true);
      Decimal v2 = (Decimal)field2.get(source);
      productSum = productSum.add(v1.multiply(v2));
      sum = sum.add(v2);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public void reset() {
    productSum = Decimal.ZERO;
    sum = Decimal.ZERO;
  }

  
  @Override
  public Object getValue() {
    if (sum.isZero()) {
      return Decimal.ZERO;
    } else {
      return productSum.divide(sum, productSum.getScale());
    }
  }
}
