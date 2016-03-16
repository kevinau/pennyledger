package org.pennyledger.db;

public interface IResultSetMetaData {

  public int getColumnCount();

  public String getColumnLabel(int i);

  public int getColumnType(int i);

  public String getColumnName(int i);

}
