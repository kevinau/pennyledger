package org.pennyledger.entitydb;

import java.util.List;


public interface IEntityDatabaseMapping {

  public static class MappingEntry {
    private final String packagePrefix;
    private final String databaseName;
    private final String schema;
    
    MappingEntry (String packagePrefix, String databaseName, String schema) {
      this.packagePrefix = packagePrefix;
      this.databaseName = databaseName;
      this.schema = schema;
    }
    
    public String getPackagePrefix() {
      return packagePrefix;
    }
    
    public String getDatabaseName() {
      return databaseName;
    }
    
    public String getSchema () {
      return schema;
    }
  }
  
  public String getName();


  public List<MappingEntry> getEntityMappings();

}