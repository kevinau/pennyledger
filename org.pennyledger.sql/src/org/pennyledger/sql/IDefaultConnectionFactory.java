package org.pennyledger.sql;


public interface IDefaultConnectionFactory {

  public IConnection getIConnection();
  
  public java.sql.Connection getConnection();
  
}
