/*******************************************************************************
s * Copyright (c) 2012 Kevin Holloway (kholloway@geckosoftware.co.uk).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 *******************************************************************************/
package org.pennyledger.form.type;

import org.pennyledger.util.UserEntryException;


public class LongType extends IntegerBasedType<Long> {
  
  public LongType () {
    super (Long.MIN_VALUE, Long.MAX_VALUE);
  }


  public LongType (long min, long max) {
    super (min, max);
  }


  @Override
  protected Long createFromString(String source, Long fillValue) throws UserEntryException {
    validateIntegerSource(source);
    return Long.parseLong(source);
  }


  @Override
  protected long longValue (Long value) {
    return value;
  }
  
  
  @Override
  public Long newInstance(String source) {
    return Long.parseLong(source);
  }


  @Override
  public Long primalValue() {
    return 0L;
  }


  @Override
  protected void validate (Long value) throws UserEntryException {
    checkWithinRange(value);
  }

}
