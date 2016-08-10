package org.pennyledger.reportdb;

import org.pennyledger.math.Decimal;

public class DecimalCount implements IAccumulationColumn {

  public int count;
  
  @Override
  public void accumulate(Object source) {
    count++;
  }

  @Override
  public void reset() {
    count = 0;
  }

  @Override
  public Object getValue() {
    return new Decimal(count);
  }

}
