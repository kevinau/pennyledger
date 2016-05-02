package org.pennyledger.entitydb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.pennyledger.object.plan.IClassPlan;


public class ResultSetIterator<T> implements Iterator<T> {

  private final String sql;
  private final PreparedStatement ps;
  private final IClassPlan<T> plan;
  
  private ResultSet rs;


  public ResultSetIterator(String sql, PreparedStatement ps, IClassPlan<T> plan) {
    this.ps = ps;
    this.sql = sql;
    this.plan = plan;
  }


  public void init() {
    try {
      rs = ps.executeQuery();
    } catch (SQLException ex) {
      close();
      throw new RuntimeException(ex);
    }
  }


  @Override
  public boolean hasNext() {
    if (ps == null) {
      init();
    }
    try {
      boolean hasMore = rs.next();
      if (!hasMore) {
        close();
      }
      return hasMore;
    } catch (SQLException ex) {
      close();
      throw new RuntimeException(ex);
    }

  }


  private void close() {
    try {
      ps.close();
    } catch (SQLException ex) {
      // nothing we can do here
    }
  }


  @Override
  public T next() {
    try {
      return SQL.rowAsTuple(sql, rs);
    } catch (DataAccessException e) {
      close();
      throw e;
    }
  }
}
