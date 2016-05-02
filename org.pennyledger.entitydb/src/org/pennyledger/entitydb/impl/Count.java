package org.pennyledger.entitydb.impl;

public class Count implements IAccumulation {

  private int count= 0;
  
  @Override
  public void accumulate (Object data) {
    count++;
  }

  @Override
  public Integer get() {
    return count;
  }
  
}
