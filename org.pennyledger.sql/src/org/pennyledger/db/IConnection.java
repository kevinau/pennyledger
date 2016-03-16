package org.pennyledger.db;

import org.pennyledger.sql.dialect.IDialect;

public interface IConnection {

  public IPreparedStatement prepareStatement (String sql);
  
  public void close ();

  public void beginTransaction ();
  
  public void setAutoCommit(boolean b);

  public void commit();

  public void rollback();

  public java.sql.Connection getUnderlyingConnection();
  
  public void executeCommand (String sql);
  
  public IDatabaseMetaData getMetaData();
  
  public IDialect getDialect();
  
}
