package org.pennyledger.sql.conn;

import java.util.Properties;

import org.pennyledger.db.IConnection;
import org.pennyledger.db.impl.Connection;
import org.pennyledger.db.impl.DatabaseProperties;
import org.pennyledger.sql.IDefaultConnectionFactory;
import org.pennyledger.sql.dialect.IDialect;


public class SpecifiedDatabase implements IDefaultConnectionFactory {

  private final DatabaseProperties db;
  
  
  public SpecifiedDatabase (IDialect dialect, String serverName, String dbName, String userName, String password) {
    db = new DatabaseProperties(dialect, serverName, dbName, userName, password);
  }
  
  
  public SpecifiedDatabase (DatabaseProperties db) {
    this.db = db;
  }
  
  
  @Override
  public IConnection getIConnection() {
    Properties props = new Properties();
    props.put("user", db.getUserName());
    props.put("password", db.getPassword());
    java.sql.Connection conn = db.getDialect().getConnection(db.getUrl(), props);
    return new Connection(db.getDialect(), conn);
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
