package org.pennyledger.sql.dialect.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.pennyledger.sql.dialect.IDialect;
import org.osgi.service.component.annotations.Component;

/**
 * An implementation of IDialect for the Postgresql database.
 * 
 * @author Kevin Holloway
 * 
 */
@Component(property={"dialectName:String=PostgreSQL"})
public class PostgresqlDialect implements IDialect {

  private final String driverClassName = "org.postgresql.Driver";
  
  
  @Override
  public String getName() {
    return "PostgreSQL";
  }

  @Override
  public String getURLTemplate() {
    return "jdbc:postgresql://{0}/{1}";
  }


  @Override
  public Connection getConnection (String url, Properties props) {
    try {
      Class.forName(driverClassName).newInstance();
    } catch (InstantiationException ex) {
      ex.printStackTrace();
    } catch (IllegalAccessException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }

    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url, props);
    } catch (SQLException ex) {
      System.out.println(url);
      System.out.println(props);
      throw new RuntimeException(ex);
    }
    return conn;
  }

  
  @Override
  public ConnectionFactory getConnectionFactory(String url, Properties props) {
    try {
      Class.forName(driverClassName).newInstance();
    } catch (InstantiationException ex) {
      ex.printStackTrace();
    } catch (IllegalAccessException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }

    return new DriverManagerConnectionFactory(url, props);
  }

  @Override
  public String dropTableTemplate() {
    return "DROP TABLE {} IF EXISTS";
  }

  @Override
  public String idColumnTemplate() {
    return "id integer SERIAL PRIMARY KEY";
  }
  

}
