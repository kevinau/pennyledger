package org.pennyledger.entitydb;

import java.util.List;

import org.pennyledger.db.IConnection;

public interface ITableSet {

  public java.sql.Connection getConnection();
  
  public List<String> getEntityPrefixes();
  
  public IConnection getIConnection();
  
  public String getName();
  
  public String getSchema();
  
}
