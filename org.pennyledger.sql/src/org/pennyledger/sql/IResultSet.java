package org.pennyledger.sql;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import org.pennyledger.math.Decimal;

public interface IResultSet {

  public Object getObject(int i, int[] colTypes);

  public Object getObject(int[] colTypes);

  public <T> T getEnum(int i, Class<T> enumClass);

  public <T> T getEnum(Class<T> enumClass);

  public byte[] getBytes(int i);

  public byte[] getBytes();

  public BigDecimal getBigDecimal(int i);

  public BigDecimal getBigDecimal();

  public BigInteger getBigInteger(int i);

  public BigInteger getBigInteger();

  public boolean getBoolean(int i);

  public boolean getBoolean();

  public int getInt(int i);

  public int getInt();

  public long getLong(int i);

  public long getLong();

  public short getShort(int i);

  public short getShort();

  public Decimal getDecimal(int i);

  public Decimal getDecimal();

  public Date getDate(int i);

  public Date getDate();

  public LocalDate getLocalDate(int i);

  public LocalDate getLocalDate();

  public URL getURL(int i);

  public URL getURL();

  public Object getObject(int i);

  public Object getObject();

  public String getString(int i);

  public String getString();

  public Timestamp getTimestamp(int i);

  public Timestamp getTimestamp();

  public boolean next();

  public void close();

  public IResultSetMetaData getMetaData();

}