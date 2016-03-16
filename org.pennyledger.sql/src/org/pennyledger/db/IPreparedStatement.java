package org.pennyledger.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import org.pennyledger.math.Decimal;

public interface IPreparedStatement {

  public void close();

  public void addBatch();

  public void executeBatch();

  public void clearParameters();

  public void setBytes(int i, byte[] bx);

  public void setBytes(byte[] bx);

  public void setNull(int i, int sqlType);

  public void setNull(int sqlType);

  public void setString(int i, String s);

  public void setString(String s);

  public void setInt(int i, int v);

  public void setInt(int v);

  public void setShort(int i, short v);

  public void setShort(short v);

  public void setLong(int i, long v);

  public void setLong(long v);

  public void setBigDecimal(int i, BigDecimal d);

  public void setBigDecimal(BigDecimal d);

  public void setBigInteger(int i, BigInteger d);

  public void setBigInteger(BigInteger d);

  public void setDate(int i, Date d);

  public void setDate(Date d);

  public void setLocalDate(int i, LocalDate d);

  public void setLocalDate(LocalDate d);

  public void setTimestamp(int i, Timestamp ts);

  public void setTimestamp(Timestamp ts);

  public void setDecimal(int i, Decimal d);

  public void setDecimal(Decimal d);

  public void setEnum(int i, Enum<?> v);

  public void setEnum(Enum<?> v);

  public void setURL(int i, URL obj);

  public void setURL(URL obj);
  
  public void setObject(int i, Object obj);

  public void setObject(Object obj);
  
  public void setFetchSize(int n);

  public IResultSet executeQuery(Object... parameters);

  public int executeUpdate();

  public int executeUpdate(Object... values);

  public Timestamp executeQueryForTimestamp(Object... values);

  public int executeQueryForInt(Object... values);

  public String executeQueryForString(Object... values);

  public long getGeneratedKey();
  
}