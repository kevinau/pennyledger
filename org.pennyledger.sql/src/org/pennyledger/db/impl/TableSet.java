package org.pennyledger.db.impl;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.db.IConnection;
import org.pennyledger.db.IDatabase;
import org.pennyledger.db.IDatabaseRegistry;
import org.pennyledger.db.ITableSet;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;


@Component(configurationPolicy=ConfigurationPolicy.REQUIRE, immediate=true)
public class TableSet implements ITableSet {

  @Configurable(required=true)
  private String name;
  
  
  @Configurable(required=true)
  private String databaseName;
  
  
  @Configurable
  private String schema;
  
  
  @Configurable(required=true)
  private List<String> table;
  
  
  private IDatabaseRegistry databaseRegistry;
  
  
  @Reference
  public void setDatabaseRegistry(IDatabaseRegistry databaseRegistry) {
    this.databaseRegistry = databaseRegistry;
  }


  public void unsetDatabaseRegistry(IDatabaseRegistry databaseRegistry) {
    this.databaseRegistry = null;
  }


  @Activate
  public void activate (ComponentContext context) {
    ComponentConfiguration.load(this, context);
  }
  
  
  @Deactivate
  public void deativate (ComponentContext context) {
  }
  
  
  @Override
  public String getName() {
    return name;
  }


  @Override
  public IConnection getIConnection() {
    IDatabase database = databaseRegistry.getDatabase(databaseName);
    return database.getIConnection();
  }
  
  
  @Override
  public java.sql.Connection getConnection() {
    IDatabase database = databaseRegistry.getDatabase(databaseName);
    return database.getConnection();
  }
  
  
  @Override
  public String getSchema() {
    return schema;
  }


  @Override
  public List<String> getTablePrefixes() {
    return table;
  }
  
}
