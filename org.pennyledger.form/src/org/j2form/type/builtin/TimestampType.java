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


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.pennyledger.util.UserEntryException;
import org.pennyledger.value.TimestampValue;


public class TimestampType extends StringBasedType<TimestampValue> {

  private static final String REQUIRED_MESSAGE = "a date/time is required";
  
  private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  
  public TimestampType () {
  	super (10 + 1 + 8);
  }
  
  
  @Override
  public String getRequiredMessage () {
    return REQUIRED_MESSAGE;
  }
  
  
  @Override
  public TimestampValue createFromString (String source) throws UserEntryException {
    try {
      long x = format.parse(source).getTime();
      Timestamp t = new Timestamp(x);
      return new TimestampValue(t);
    } catch (ParseException ex) {
      throw new UserEntryException("invalid date/time");
    }
  }
  
  
  @Override
  public TimestampValue primalValue () {
    return new TimestampValue(new Timestamp(0L));
  }
  
  
  @Override
  public TimestampValue newInstance (String source) {
    try {
      long x = format.parse(source).getTime();
      Timestamp t = new Timestamp(x);
      return new TimestampValue(t);
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  protected void validate(TimestampValue value) throws UserEntryException {
    // Nothing more to do
  }

}

