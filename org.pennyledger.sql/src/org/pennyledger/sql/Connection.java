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
package org.pennyledger.sql;


import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import org.pennyledger.db.IConnection;
import org.pennyledger.db.IDatabaseMetaData;
import org.pennyledger.db.IPreparedStatement;
import org.pennyledger.math.Decimal;
import org.pennyledger.sql.dialect.IDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Connection implements IConnection {

  private static final Logger logger = LoggerFactory.getLogger(Connection.class);
  
  private final IDialect dialect;
  private final java.sql.Connection conn;

  private boolean logSQL = true;
  
  
  public Connection (IDialect dialect, java.sql.Connection conn) {
    this.dialect = dialect;
    this.conn = conn;
  }
  
  
//  static String toSQLName (String name) {
//    StringBuffer buffer = new StringBuffer();
//    boolean insertUnderscore = false;
//    int n = name.length();
//    for (int i = 0; i < n; i++) {
//      char c = name.charAt(i);
//      if (i == 0) {
//         buffer.append(Character.toLowerCase(c));
//      } else {
//        if (insertUnderscore && Character.isUpperCase(c)) {
//          buffer.append('_');
//          insertUnderscore = false;
//          buffer.append(Character.toLowerCase(c));
//        } else {
//          buffer.append(c);
//        }
//        if (!Character.isUpperCase(c)) {
//          insertUnderscore = true;
//        }
//           
//      }
//    }
//    return buffer.toString();
//  }
  

  static void setValue (java.sql.PreparedStatement stmt, int i, Object value) {
    try {
      if (value instanceof Decimal) {
        stmt.setBigDecimal(i, new BigDecimal(((Decimal)value).toString()));
      } else if (value instanceof LocalDate) {
        stmt.setDate(i, new java.sql.Date(((LocalDate)value).toEpochDay()));
      } else {
        stmt.setObject(i, value);
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public IPreparedStatement prepareStatement (String sql) {
    try {
      java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
      return new PreparedStatement(sql, stmt);
    } catch (SQLException ex) {
      throw new RuntimeException(sql, ex);
    }
  }

  
  @Override
  public void beginTransaction () {
    try {
      conn.setAutoCommit(false);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  @Override
  public void rollback () {
    try {
      conn.rollback();
      conn.setAutoCommit(true);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void setAutoCommit (boolean auto) {
    try {
      conn.setAutoCommit(auto);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void commit () {
    try {
      conn.commit();
      conn.setAutoCommit(true);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void close () {
    try {
      conn.close();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public java.sql.Connection getUnderlyingConnection () {
    return conn;
  }
  
  
  @Override
  public void executeCommand (String sql) {
    try {
      java.sql.Statement stmt = conn.createStatement();
      if (logSQL) {
        logger.info(sql);
      }
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (SQLException ex) {
      throw new RuntimeException(sql, ex);
    }
  }
  
  
  @Override
  public IDatabaseMetaData getMetaData() {
    try {
      return new DatabaseMetaData(conn.getMetaData());
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
    
  
  @Override
  public IDialect getDialect () {
    return dialect;
  }

}
