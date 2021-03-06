/*******************************************************************************
 * Copyright (c) 2012 Kevin Holloway (kholloway@geckosoftware.co.uk).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 *******************************************************************************/
package org.pennyledger.object.type.builtin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pennyledger.object.UserEntryException;


public class BigIntegerType extends IntegerBasedType<BigInteger> {
  
  private static final BigInteger DEFAULT_MAX = new BigInteger("999999999999");
  private static final BigInteger DEFAULT_MIN = new BigInteger("-99999999999");
  
  public BigIntegerType () {
    super (DEFAULT_MIN, DEFAULT_MAX);
  }


  public BigIntegerType (BigInteger min, BigInteger max) {
    super (min, max);
  }


  @Override
  public BigInteger createFromString(String source) throws UserEntryException {
    validateIntegerSource(source);
    return new BigInteger(source);
  }


  @Override
  protected long longValue (BigInteger value) {
    return value.longValue();
  }
  
  
  @Override
  public BigInteger newInstance(String source) {
    return new BigInteger(source);
  }


  @Override
  public BigInteger primalValue() {
    return BigInteger.ZERO;
  }


  @Override
  public JsonType getJsonType () {
    return JsonType.NUMBER;
  }
  
  
  @Override
  protected void validate (BigInteger value) throws UserEntryException {
    checkWithinRange(value.longValue());
  }

  
  @Override
  public String getSQLType() {
    return "DECIMAL(" + getMaxDigits() + ")";
  }


  @Override
  public void setSQLValue(PreparedStatement stmt, int sqlIndex, BigInteger value) throws SQLException {
    stmt.setBigDecimal(sqlIndex, new BigDecimal(value));
  }


  @Override
  public BigInteger getSQLValue(ResultSet resultSet, int sqlIndex) throws SQLException {
    return resultSet.getBigDecimal(sqlIndex).toBigInteger();
  }

}
