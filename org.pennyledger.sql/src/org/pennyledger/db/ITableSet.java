package org.pennyledger.db;

import java.util.List;

public interface ITableSet {

  public String getName();
  
  public IConnection getIConnection();
  
  public java.sql.Connection getConnection();
  
  public String getSchema();
  
  public List<String> getTablePrefixes();
  
}
