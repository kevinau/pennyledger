package org.pennyledger.entitydb.impl;

import java.util.Comparator;

public class SchemaTableName implements Comparator<SchemaTableName> {

  private final String fqTableName;
  
  public SchemaTableName (String schema, String tableName) {
    if (tableName == null) {
      throw new IllegalArgumentException("Table name parameter cannot be null");
    }
    if (schema == null || schema.length() == 0) {
      fqTableName = tableName;
    } else {
      fqTableName = schema + "." + tableName;
    }
  }
  
  @Override
  public String toString() {
    return fqTableName;
  }

  @Override
  public int hashCode() {
    return fqTableName.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return fqTableName.equals(obj);
  }

  @Override
  public int compare(SchemaTableName arg0, SchemaTableName arg1) {
    return arg0.fqTableName.compareTo(arg1.fqTableName);
  }
  
}
