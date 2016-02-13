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

import org.pennyledger.form.NumberSign;
import org.pennyledger.util.UserEntryException;


public class DoubleType extends DecimalBasedType<Double> {

  private static final Double ZERO = new Double(0);
  

  public DoubleType () {
    super (10, 0);
  }

  
  public DoubleType (int precision) {
    super (precision);
  }

  
  public DoubleType (int precision, int decimals) {
    super (precision, decimals);
  }

  
  public DoubleType (NumberSign sign, int precision) {
    super (sign, precision);
  }

  
  public DoubleType (NumberSign sign, int precision, int decimals) {
    super (sign, precision, decimals);
  }


  @Override
  protected Double createFromString(String source, Double fillValue) throws UserEntryException {
    validateDecimalSource (source);
    return Double.parseDouble(source);
  }


  @Override
  public Double newInstance(String source) {
    return Double.parseDouble(source);
  }


  @Override
  public Double primalValue() {
    return ZERO;
  }
  
  
  @Override
  protected void validate(Double value) throws UserEntryException {
    validatePrecision(value.longValue());
  }
  
}
