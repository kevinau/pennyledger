package org.pennyledger.db;

public interface IEntityResultSet<T> extends IResultSet {

  public T getEntity();

}