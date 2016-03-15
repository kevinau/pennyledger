package org.pennyledger.sql.dialect.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.osgi.service.component.annotations.Component;
import org.pennyledger.sql.dialect.IDialect;


/**
 * An implementation of IDialect for the Derby database.
 * 
 * @author Kevin Holloway
 * 
 */
@Component(property={"dialectName:String=DerbyServer"})
public class DerbyServerDialect implements IDialect {

  @Override
  public String getName () {
    return "DerbyServer";
  }
  
  @Override
  public String getURLTemplate () {
    return "jdbc:derby://{0}/{1};create=true";
  }
  
  
  @Override
  public String dropTableTemplate () {
    return "DROP TABLE {0}";
  }
  
  @Override
  public Connection getConnection (String url, Properties props) {
    // This uses the database driver: org.apache.derby.jdbc.ClientDriver
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, props);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
    return conn;
  }

  
  @Override
  public ConnectionFactory getConnectionFactory(String url, Properties props) {
    return new DriverManagerConnectionFactory(url, props);
  }
  
  @Override
  public String idColumnTemplate () {
    return "id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY";
  }
  
}
