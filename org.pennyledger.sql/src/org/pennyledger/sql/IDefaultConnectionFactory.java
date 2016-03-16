package org.pennyledger.sql;

import org.pennyledger.db.IConnection;

public interface IDefaultConnectionFactory {

  public IConnection getIConnection();
  
  public java.sql.Connection getConnection();
  
}
