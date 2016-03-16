package org.pennyledger.db;

public interface IDatabaseRegistry {

  public String[] getDatabaseNames();

  public IDatabase getDatabase(String name);

}