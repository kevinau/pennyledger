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
package org.j2form.type.builtin;

import org.j2form.NumberSign;
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

}
