package org.pennyledger.sql;

import java.text.MessageFormat;

import org.pennyledger.sql.dialect.IDialect;

public class DatabaseProperties {

  private final String url;
  private final IDialect dialect;
  private final String userName;
  private final String password;

  
  public DatabaseProperties (IDialect dialect, String serverName, String dbName, String userName, String password) {
    this.dialect = dialect;
    this.userName = userName;
    this.password = password;
    this.url = MessageFormat.format(dialect.getURLTemplate(), serverName, dbName);
  }


  public String getUrl() {
    return url;
  }


  public IDialect getDialect() {
    return dialect;
  }


  public String getUserName() {
    return userName;
  }


  public String getPassword() {
    return password;
  }

}
