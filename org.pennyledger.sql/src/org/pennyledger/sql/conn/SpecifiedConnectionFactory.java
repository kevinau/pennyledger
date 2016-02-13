package org.pennyledger.sql.conn;

import java.util.Properties;

import org.pennyledger.sql.Connection;
import org.pennyledger.sql.DatabaseProperties;
import org.pennyledger.sql.IConnection;
import org.pennyledger.sql.IDefaultConnectionFactory;
import org.pennyledger.sql.dialect.IDialect;


public class SpecifiedConnectionFactory implements IDefaultConnectionFactory {

  private final DatabaseProperties db;
  
  
  public SpecifiedConnectionFactory (IDialect dialect, String serverName, String dbName, String userName, String password) {
    db = new DatabaseProperties(dialect, serverName, dbName, userName, password);
  }
  
  
  public SpecifiedConnectionFactory (DatabaseProperties db) {
    this.db = db;
  }
  
  
  @Override
  public IConnection getIConnection() {
    Properties props = new Properties();
    props.put("user", db.getUserName());
    props.put("password", db.getPassword());
    java.sql.Connection conn = db.getDialect().getConnection(db.getUrl(), props);
    return new Connection(conn);
  }
  
  
  @Override
  public java.sql.Connection getConnection() {
    Properties props = new Properties();
    props.put("user", db.getUserName());
    props.put("password", db.getPassword());
    java.sql.Connection conn = db.getDialect().getConnection(db.getUrl(), props);
    return conn;
  }
  
}
