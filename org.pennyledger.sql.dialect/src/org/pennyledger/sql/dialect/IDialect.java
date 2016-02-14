/*******************************************************************************
 * Copyright (c) 2008 Kevin Holloway (kholloway@geckosoftware.com.au).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.txt
 * 
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 ******************************************************************************/
package org.pennyledger.sql.dialect;

import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.dbcp.ConnectionFactory;

/**
 * An SQL dialect (such as Postgresql, Derby, etc). Classes that implement this
 * interface provide enough information to create a database connection.
 * 
 * @author Kevin Holloway
 * 
 */
public interface IDialect {

  /**
   * The name of this SQL dialect.
   */
  public String getName();

  /**
   * A MessageFormat string that allows a URL to be created using a server and
   * database name.
   */
  public String getURLTemplate();

  /**
   * A method that returns a database connection given a URL and a set of
   * properties. The properties will include username and password.
   * 
   * @param url
   *          - a String that contains a URL. The URL will have been obtained
   *          from the getURLTemplate or provided directly.
   * @param props
   *          - a Properties object that contains a username and password.
   * @return a database Connection.
   */
  public Connection getConnection(String url, Properties props);

  /**
   * A method that returns a database connection factory given a URL and a set
   * of properties. The properties will include username and password.
   * 
   * @param url
   *          - a String that contains a URL. The URL will have been obtained
   *          from the getURLTemplate or provided directly.
   * @param props
   *          - a Properties object that contains a username and password.
   * @return a database connection factory. This will allow database connections
   *         to be issued from a connection pool.
   */
  public ConnectionFactory getConnectionFactory(String url, Properties props);
  
}
