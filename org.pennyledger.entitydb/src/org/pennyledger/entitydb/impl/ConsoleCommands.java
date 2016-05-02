package org.pennyledger.entitydb.impl;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.db.IConnection;
import org.pennyledger.db.IDatabase;
import org.pennyledger.db.IDatabaseRegistry;
import org.pennyledger.entity.IEntityRegistry;
import org.pennyledger.entitydb.IEntityDatabaseMapping.MappingEntry;
import org.pennyledger.entitydb.IEntityDatabaseMappingRegistry;

@Component(
    property = {"osgi.command.scope:String=entitydb",
                "osgi.command.function:String=list",
                "osgi.command.function:String=create",
    },
    service = Object.class)
public class ConsoleCommands {

  private IDatabaseRegistry databaseRegistry;
  private IEntityDatabaseMappingRegistry mappingRegistry;
  private IEntityRegistry entityRegistry;
  
  @Reference
  public void setDatabaseRegistry (IDatabaseRegistry databaseRegistry) {
    this.databaseRegistry = databaseRegistry;
  }
  
  public void unsetDatabaseRegistry (IDatabaseRegistry databaseRegistry) {
    this.databaseRegistry = null;
  }
  
  @Reference
  public void setEntityDatabaseMappingRegistry (IEntityDatabaseMappingRegistry mappingRegistry) {
    this.mappingRegistry = mappingRegistry;
  }
  
  public void unsetEntityDatabaseMappingRegistry (IEntityDatabaseMappingRegistry mappingRegistry) {
    this.mappingRegistry = null;
  }
  
  @Reference
  public void setEntityRegistry (IEntityRegistry entityRegistry) {
    this.entityRegistry = entityRegistry;
  }
  
  public void unsetEntityRegistry (IEntityRegistry entityRegistry) {
    this.entityRegistry = null;
  }
  
  public Object list() {
    String[] mappingNames = mappingRegistry.getMappingNames();
    for (String mappingName : mappingNames) {
      System.out.println(mappingName);
    }
    return null;
  }

  public Object create(String name) {
    List<MappingEntry> mappings = Collections.emptyList();
    
    try {
      mappings = mappingRegistry.getMappingEntries(name);
    } catch (IllegalArgumentException ex) {
      System.err.println("'" + name + "' does not name a known entity-database mapping.");
      System.err.println("Try the entitydb:list command to see all known entity-database mappings.");
    }

    try {
      for (MappingEntry mapping : mappings) {
        String databaseName = mapping.getDatabaseName();
        IDatabase database = databaseRegistry.getDatabase(databaseName);
        IConnection conn = database.getIConnection();
        
        DatabaseTableCreator tableCreator = new DatabaseTableCreator(conn);
        String packagePrefix = mapping.getPackagePrefix();
        String schema = mapping.getSchema();
        tableCreator.dropAndCreateTableSet(System.out, packagePrefix, schema, entityRegistry);        
      }
    } catch (Exception ex) {
      ex.printStackTrace(System.err);
      throw ex;
    }
    return null;
  }

}
