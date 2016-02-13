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

import org.pennyledger.util.UserEntryException;


public class ShortType extends IntegerBasedType<Short> {
  
  public ShortType () {
    super (Short.MIN_VALUE, Short.MAX_VALUE);
  }


  public ShortType (short min, short max) {
    super (min, max);
  }


  @Override
  protected Short createFromString(String source, Short fillValue) throws UserEntryException {
    validateIntegerSource(source);
    return Short.parseShort(source);
  }


  @Override
  protected long longValue (Short value) {
    return value;
  }
  
  
  @Override
  public Short newInstance(String source) {
    return Short.parseShort(source);
  }


  @Override
  public Short primalValue() {
    return 0;
  }


  @Override
  protected void validate (Short value) throws UserEntryException {
    checkWithinRange(value);
  }

}
