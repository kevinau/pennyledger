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


public class FloatType extends DecimalBasedType<Float> {

  public FloatType () {
    super (8, 0);
  }

  
  public FloatType (int precision) {
    super (precision);
  }

  
  public FloatType (int precision, int decimals) {
    super (precision, decimals);
  }

  
  public FloatType (NumberSign sign, int precision) {
    super (sign, precision);
  }

  
  public FloatType (NumberSign sign, int precision, int decimals) {
    super (sign, precision, decimals);
  }


  @Override
  protected Float createFromString(String source, Float fillValue) throws UserEntryException {
    validateDecimalSource (source);
    return Float.parseFloat(source);
  }


  @Override
  public Float newInstance(String source) {
    return Float.parseFloat(source);
  }


  @Override
  public Float primalValue() {
    return 0F;
  }
  
  
  @Override
  protected void validate(Float value) throws UserEntryException {
    validatePrecision(value.longValue());
  }
  
}