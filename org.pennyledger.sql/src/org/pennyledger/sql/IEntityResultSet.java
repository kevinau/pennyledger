package org.pennyledger.sql;

public interface IEntityResultSet<T> extends IResultSet {

  public T getEntity();

}