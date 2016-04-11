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
package org.pennyledger.form.type.builtin;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pennyledger.form.NumberSign;
import org.pennyledger.math.Decimal;
import org.pennyledger.util.UserEntryException;


public class DecimalType extends DecimalBasedType<Decimal> {

  public DecimalType () {
    super (10, 0);
  }
  
  
  public DecimalType (NumberSign sign, int precision, int scale) {
    super (sign, precision, scale);
  }

  
  public DecimalType (NumberSign sign, int precision) {
    super (sign, precision);
  }

  
  public DecimalType (int precision, int scale) {
    super (precision, scale);
  }

  
  public DecimalType (int precision) {
    super (precision);
  }

  
  @Override
  public Decimal primalValue() {
    return Decimal.ZERO;
  }


  @Override
  public JsonType getJsonType () {
    return JsonType.PLAIN_STRING;
  }
  
  
  @Override
  public Decimal newInstance(String source) {
    return new Decimal(source);
  }


  @Override
  public Decimal createFromString(String source) throws UserEntryException {
    validateDecimalSource(source);
    return new Decimal(source);
  }


  @Override
  protected void validate(Decimal value) throws UserEntryException {
    validatePrecision(value.longValue());
    validateDecimals(value.trim().getScale());
  }


  @Override
  public void setSQLValue(PreparedStatement stmt, int sqlIndex, Decimal value) throws SQLException {
    stmt.setBigDecimal(sqlIndex, new BigDecimal(value.toString()));
  }


  @Override
  public Decimal getSQLValue(ResultSet resultSet, int sqlIndex) throws SQLException {
    BigDecimal bd = resultSet.getBigDecimal(sqlIndex);
    return new Decimal(bd.toString());
  }

}
