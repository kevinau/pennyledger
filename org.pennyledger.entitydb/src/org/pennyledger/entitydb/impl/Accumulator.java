package org.pennyledger.entitydb.impl;

public interface Accumulator<A,T> {

  public void accumulate (A total, T object);
  
}
