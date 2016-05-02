package org.pennyledger.entitydb.impl;

import org.pennyledger.object.type.IType;

public class AccumulationValueSet implements IValueSet {

  private final IAccumulation[] accums;
  private final IType<?>[] types;
  
  
  public AccumulationValueSet (IAccumulation[] accums, IType<?>[] types) {
    this.accums = accums;
    this.types = types;
  }
  
  
  @Override
  public int size() {
    return accums.length;
  }
  
  
  @Override
  public Object get(int index) {
    return accums[index].get();
  }

  
  @Override
  public IType<?> typeOf(int index) {
    return types[index];
  }
  
}
