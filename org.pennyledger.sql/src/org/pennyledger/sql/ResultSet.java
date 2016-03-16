package org.pennyledger.sql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.TimeZone;

import org.pennyledger.db.IResultSet;
import org.pennyledger.db.IResultSetMetaData;
import org.pennyledger.db.ResultSetMetaData;
import org.pennyledger.math.Decimal;

public class ResultSet implements IResultSet {
  
  static final Calendar tzCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

  protected final java.sql.ResultSet rs;
  
  private int index = 0;
  
  ResultSet (java.sql.ResultSet rs) {
    this.rs = rs;
  }
  
  
  @Override
  public Object getObject (int i, int[] colType) {
    try {
      switch (colType[i - 1]) {
      case Types.CHAR :
        String x1 = rs.getString(i);
        if (x1 != null) {
          return x1.trim();
        } else {
          return x1;
        }
      case Types.DATE :
        Date d = rs.getDate(i);
        if (d != null) {
          return LocalDate.ofEpochDay(d.getTime());
        } else {
          return d;
        }
      case Types.NUMERIC :
        String x2 = rs.getString(i);
        if (x2 != null) {
          return new Decimal(x2);
        } else {
          return x2;
        }
      default :
        return rs.getObject(i);
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public Object getObject (int[] colType) {
    return getObject(++index, colType);
  }
  
  
  @Override
  public <T> T getEnum (int i, Class<T> enumClass) {
    T[] values = enumClass.getEnumConstants();
    
    try {
      // TODO This class has Postgresql dependences
      //org.postgresql.util.PGobject obj = (org.postgresql.util.PGobject)rs.getObject(i);
      String target = rs.getString(i).toUpperCase();
      for (T value : values) {
        if (value.toString().equals(target)) {
          return value;
        }
      }
      throw new IllegalArgumentException(target);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  

  @Override
  public <T> T getEnum (Class<T> enumClass) {
    return getEnum(++index, enumClass);
  }
  
  
  @Override
  public BigDecimal getBigDecimal (int i) {
    try {
      return rs.getBigDecimal(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public BigDecimal getBigDecimal () {
    return getBigDecimal(++index);
  }
  
  
  @Override
  public BigInteger getBigInteger (int i) {
    try {
      BigDecimal value = rs.getBigDecimal(i);
      return value.toBigInteger();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public BigInteger getBigInteger () {
    return getBigInteger(++index);
  }
  
  
  @Override
  public byte[] getBytes (int i) {
    try {
      return rs.getBytes(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public byte[] getBytes () {
    return getBytes(++index);
  }
  
  
  @Override
  public boolean getBoolean (int i) {
    try {
      return rs.getBoolean(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public boolean getBoolean () {
    return getBoolean(++index);
  }
  
  
  @Override
  public int getInt (int i) {
    try {
      return rs.getInt(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public int getInt () {
    return getInt(++index);
  }
  
  
  @Override
  public long getLong (int i) {
    try {
      return rs.getLong(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public long getLong () {
    return getLong(++index);
  }
  
  
  @Override
  public short getShort (int i) {
    try {
      return rs.getShort(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public short getShort () {
    return getShort(++index);
  }
  
  
  @Override
  public Decimal getDecimal (int i) {
    try {
      BigDecimal bd = rs.getBigDecimal(i);
      return new Decimal(bd.toString());
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public Decimal getDecimal () {
    return getDecimal(++index);
  }
  
  
  @Override
  public LocalDate getLocalDate (int i) {
    try {
      java.util.Date d = (java.util.Date)rs.getDate(i, tzCal);
      return LocalDate.ofEpochDay(d.getTime());
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public LocalDate getLocalDate () {
    return getLocalDate(++index);
  }
  
  
  @Override
  public Date getDate (int i) {
    try {
      Date d = rs.getDate(i, tzCal);
      return d;
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public Date getDate () {
    return getDate(++index);
  }
  
  
  @Override
  public URL getURL (int i) {
    try {
      String s = rs.getString(i);
      return new URL(s);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    } catch (MalformedURLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public URL getURL () {
    return getURL(++index);
  }
  
  
  @Override
  public Object getObject (int i) {
    try {
      return rs.getObject(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public Object getObject () {
    return getObject(++index);
  }
  
  
  @Override
  public String getString (int i) {
    try {
      return rs.getString(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public String getString () {
    return getString(++index);
  }
  
  
  @Override
  public Timestamp getTimestamp (int i) {
    try {
      return rs.getTimestamp(i);
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public Timestamp getTimestamp () {
    return getTimestamp(++index);
  }
  
  
  @Override
  public boolean next () {
    try {
      index = 0;
      return rs.next();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void close () {
    try {
      rs.close();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public IResultSetMetaData getMetaData() {
    try {
      return new ResultSetMetaData(rs.getMetaData());
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }
}
