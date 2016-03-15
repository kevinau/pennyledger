package org.pennyledger.sql.conn;

import java.text.MessageFormat;
import java.util.Properties;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.pennyledger.sql.Connection;
import org.pennyledger.sql.IConnection;
import org.pennyledger.sql.IDefaultConnectionFactory;
import org.pennyledger.sql.dialect.DialectRegistry;
import org.pennyledger.sql.dialect.IDialect;


@Component(configurationPolicy=ConfigurationPolicy.REQUIRE)
public class DefaultDatabase implements IDefaultConnectionFactory {

  @Configurable(name="dialect", required=true)
  private String dialectName;
  
  @Configurable(name="server", required=true)
  private String serverName;
  
  @Configurable(required=true)
  private String dbname;
  
  @Configurable(required=true)
  private String username;

  @Configurable(required=true)
  private String password;

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
    System.out.println("Start of default connection factory");
    ComponentConfiguration.load(this, context);
    System.out.println("... " + dialectName);
  }
  
  
  @Override
  public IConnection getIConnection() {
    IDialect dialect = dialectRegistry.getDialect(dialectName);
    String url = MessageFormat.format(dialect.getURLTemplate(), serverName, dbname);

    Properties props = new Properties();
    props.put("user", username);
    props.put("password", password);
    
    java.sql.Connection conn = dialect.getConnection(url, props);
     return new Connection(dialect, conn);
  }
  
  
  @Override
  public java.sql.Connection getConnection() {
    IDialect dialect = dialectRegistry.getDialect(dialectName);
    String url = MessageFormat.format(dialect.getURLTemplate(), serverName, dbname);

    Properties props = new Properties();
    props.put("user", username);
    props.put("password", password);
    
    java.sql.Connection conn = dialect.getConnection(url, props);
    return conn;
  }
  
}