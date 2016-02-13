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
import org.pennyledger.form.TextCase;
import org.pennyledger.util.UserEntryException;


public abstract class IntegerBasedType<T extends Comparable<T>> extends Type<T> implements IType<T>, ITextBasedInput, IPrecisionableType {

  private long min;
  private long max;
  

  protected IntegerBasedType () {
  }
  
  
  protected IntegerBasedType (T min, T max) {
    this.min = longValue(min);
    this.max = longValue(max);
    if (this.min > this.max) {
      throw new IllegalArgumentException("min value is greater than max value");
    }
  }
  
  
  protected abstract long longValue (T value);

  
  @Override
  public TextCase getAllowedCase() {
    return TextCase.UNSPECIFIED;
  }


  private static final long[] limits = {
    9L,
    99L,
    999L,
    9999L,
    99999L,
    999999L,
    9999999L,
    99999999L,
    999999999L,
    9999999999L,
    99999999999L,
    999999999999L,
    9999999999999L,
    99999999999999L,
    999999999999999L,
    9999999999999999L,
    99999999999999999L,
    999999999999999999L,
    Long.MAX_VALUE,
  };
  
  
  private int getDigits (long value) {
    value = Math.abs(value);
    int i = 0;
    while (i < limits.length && value > limits[i]) {
      i++;
    }
    i++;
    return i;
  }
  
  
  protected int getMaxDigits () {
    long maxValue = Math.max(Math.abs(min), Math.abs(max));
    return getDigits(maxValue);
  }
  
  
  @Override
  public int getFieldSize () {
    int n = getDigits(min) + (min < 0 ? 1 : 0);
    int m = getDigits(max);
    return Math.max(n, m);
  }


  @Override
  public String getRequiredMessage() {
    return "number required (in the range " + min + " to " + max + ")";
  }
  
  
  @Override
  public abstract T newInstance (String source);


  @Override
  public abstract T primalValue();


  public void setMinMax (T min, T max) {
    this.min = longValue(min);
    this.max = longValue(max);
    if (this.min > this.max) {
      throw new IllegalArgumentException("min value is greater than max value");
    }
  }
  

  protected void validateIntegerSource (String source) throws UserEntryException {
    int i = 0;
    int length = source.length();
    
    if (length == 0) {
      throw new UserEntryException(getRequiredMessage(), UserEntryException.Type.REQUIRED);
    }
    
    /* Get the sign, but don't check it yet. */
    if (i < length) {
      char signChar = source.charAt(i);
      switch (signChar) {
      case '+' :
      case '-' :
        if (min >= 0) {
          throw new UserEntryException("no sign allowed");
        }
        i++;
        if (i == length) {
          String msg;
          if (min >= 0) {
            msg = "not a number";
            throw new UserEntryException(msg);
          } else {
            msg = "not a signed number";
            throw new UserEntryException(msg, UserEntryException.Type.INCOMPLETE);
          }
        }
        break;
      }
    }
    /* Check the digits first.  What they have entered should always be
     * checked before reporting on what they have not entered. */
    int count = 0;
    while (i < length) {
      char c = source.charAt(i);
      if (Character.isDigit(c)) {
        /* Don't count leading zero's */
        if (c != '0' || count > 0) {
          count++;
        }
      } else {
        String msg;
        if (min >= 0) {
          msg = "not a number";
        } else {
          msg = "not a signed number";
        }
        throw new UserEntryException(msg);
      }
      i++;
    }
    checkWithinRange(Long.parseLong(source));
  }
  
  
  protected void checkWithinRange (long value) throws UserEntryException {
    if (value < min || value > max) {
      throw new UserEntryException("number not in the range " + min + " to " + max);
    }
  }
  
  
  @Override
  public void setPrecision (NumberSign sign, int precision) {
    if (precision < 1 || precision > limits.length) {
      throw new IllegalArgumentException("precision " + precision);
    }
    this.max = limits[precision - 1];
    switch (sign) {
    case UNSIGNED : 
      this.min = 0;
      break;
    case SIGNED :
      this.min = -limits[precision - 1];
      break;
    case RARESIGN :
      this.min = -(max / 10);
      break;
    }
  }
  

  
  //public void validate (T value, boolean optional, boolean creating) throws UserEntryException {
  //  if (value == null) {
  //    if (optional) {
  //      return;
  //    } else {
  //      throw new UserEntryException(getRequiredMessage(), UserEntryException.Type.REQUIRED);
  //    }
  //  }
  //  validate (value);
    //BigDecimal dv = (BigDecimal)value;
    //if (sign == NumberSign.UNSIGNED && dv.compareTo(BigDecimal.ZERO) < 0) {
    //  throw new UserEntryException("negative number not allowed");
    //}
    //if (count > digits) {
    //  throw new UserEntryException("only " + digits + " digits allowed");
    //}
    //if (decimalSeen && length - decimalOffset > decimals) {
    //  throw new UserEntryException("only " + decimals + " decimal digits allowed");
    //}
    
    // check whole digits and decimal digits
  //}

}
