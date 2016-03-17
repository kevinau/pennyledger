package org.pennyledger.db;

import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(configurationPolicy=ConfigurationPolicy.REQUIRE, immediate=true)
public class TableSet implements ITableSet {

  private static final Logger logger = LoggerFactory.getLogger(TableSet.class);
  
  @Configurable(required=true)
  private String name;
  
  
  @Configurable(name="database", required=true)
  private String databaseName;
  
  
  @Configurable
  private String schema;
  
  
  @Configurable(name="table", required=true)
  private List<String> entityPrefixes;
  
  
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
    logger.info("Activate table set: {} ({} {} {})", name, databaseName, schema, entityPrefixes.size());
  }
  
  
  @Deactivate
  public void deativate (ComponentContext context) {
    logger.info("Deactivate table set: {}", name);
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
  public List<String> getEntityPrefixes() {
    return entityPrefixes;
  }
  
}
