package org.pennyledger.db;

public interface IDatabase {

  public String getName();

  public IConnection getIConnection();

  public java.sql.Connection getConnection();

}
