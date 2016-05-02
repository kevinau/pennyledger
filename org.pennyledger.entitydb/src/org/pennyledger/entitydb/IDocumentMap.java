package org.pennyledger.entitydb;

public interface IDocumentMap<T> {

  public void map (T document);
  
  public default void emit (Object... keyValue) {
    
  }
  
}
