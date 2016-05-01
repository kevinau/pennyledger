package org.gyfor.report;

public interface ResettableSupplier<T> {

  public T get();
  
  public ResettableSupplier<T> reset();
  
}
