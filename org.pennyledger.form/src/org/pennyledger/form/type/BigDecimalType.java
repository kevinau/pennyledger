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
package org.pennyledger.form.type;

import java.math.BigDecimal;

import org.pennyledger.form.NumberSign;
import org.pennyledger.util.UserEntryException;


public class BigDecimalType extends DecimalBasedType<BigDecimal> {

  private static final BigDecimal ZERO = new BigDecimal(0);
  
  
  public BigDecimalType () {
    super (10, 0);
  }

  
  public BigDecimalType (int precision) {
    super (precision);
  }

  
  public BigDecimalType (int precision, int decimals) {
    super (precision, decimals);
  }

  
  public BigDecimalType (NumberSign sign, int precision) {
    super (sign, precision);
  }

  
  public BigDecimalType (NumberSign sign, int precision, int decimals) {
    super (sign, precision, decimals);
  }


  @Override
  protected BigDecimal createFromString(String source, BigDecimal fillValue) throws UserEntryException {
    validateDecimalSource (source);
    return new BigDecimal(source);
  }


  @Override
  public BigDecimal newInstance(String source) {
    return new BigDecimal(source);
  }


  @Override
  public BigDecimal primalValue() {
    return ZERO;
  }
  
  
  @Override
  protected void validate(BigDecimal value) throws UserEntryException {
    if (getNumberSign() == NumberSign.UNSIGNED && value.compareTo(BigDecimal.ZERO) < 0) {
      throw new UserEntryException("negative number not allowed");
    }
    // The following may truncate very large numbers
    validatePrecision(value.longValue());
    validateDecimals(value.remainder(BigDecimal.ONE).stripTrailingZeros().scale());
  }
  
}
