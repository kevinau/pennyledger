package org.pennyledger.db;

public interface ITableSetRegistry {

  public String[] getTableSetNames();

  public ITableSet getTableSet(String name);

}