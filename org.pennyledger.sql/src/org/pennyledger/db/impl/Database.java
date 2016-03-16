package org.pennyledger.db.impl;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.db.IConnection;
import org.pennyledger.db.IDatabase;
import org.pennyledger.db.ITableSet;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.pennyledger.sql.Connection;
import org.pennyledger.sql.dialect.DialectRegistry;
import org.pennyledger.sql.dialect.IDialect;


@Component(configurationPolicy=ConfigurationPolicy.REQUIRE)
public class Database implements IDatabase {

  private ComponentContext context;
  
  @Configurable(required=true)
  private String dialectName;
  
  @Configurable(required=true)
  private String server;
  
  @Configurable(required=true)
  private String dbname;
  
  @Configurable(required=true)
  private String username;

  @Configurable(required=true)
  private String password;

  @Configurable(required=true)
  private String name;
  
  
  private DialectRegistry dialectRegistry;
  
  
  @Reference
  public void setDialectRegistry(DialectRegistry dialectRegistry) {
    this.dialectRegistry = dialectRegistry;
  }


  public void unsetDialectRegistry(DialectRegistry dialectRegistry) {
    this.dialectRegistry = null;
  }


  @Activate
  public void activate (ComponentContext context) {
    this.context = context;
    ComponentConfiguration.load(this, context);
  }
  
  
  @Override
  public String getName() {
    return name;
  }


  @Override
  public IConnection getIConnection() {
    IDialect dialect = dialectRegistry.getDialect(dialectName);
    String url = MessageFormat.format(dialect.getURLTemplate(), server, dbname);

    Properties props = new Properties();
    props.put("user", username);
    props.put("password", password);
    
    java.sql.Connection conn = dialect.getConnection(url, props);
    return new Connection(dialect, conn);
  }
  
  
  @Override
  public java.sql.Connection getConnection() {
    IDialect dialect = dialectRegistry.getDialect(dialectName);
    String url = MessageFormat.format(dialect.getURLTemplate(), server, dbname);

    Properties props = new Properties();
    props.put("user", username);
    props.put("password", password);
    
    java.sql.Connection conn = dialect.getConnection(url, props);
    return conn;
  }


  @Override
  public ITableSet[] getTableSets() {
    try {
      BundleContext bc = context.getBundleContext();
      Collection<ServiceReference<ITableSet>> serviceRefs = bc.getServiceReferences(ITableSet.class, "(databaseName=" + name + ")");
      ITableSet[] tableSets = new ITableSet[serviceRefs.size()];
      
      int i = 0;
      for (ServiceReference<ITableSet> serviceRef : serviceRefs) {
        tableSets[i++] = bc.getService(serviceRef);
      }
      return tableSets;
    } catch (InvalidSyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
  
}
