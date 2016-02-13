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


import java.time.LocalDate;

import org.pennyledger.util.UserEntryException;


public class LocalDateType extends DateBasedType<LocalDate> {
  
  public LocalDateType () {
  }
  
  
  @Override
  public LocalDate primalValue() {
    return LocalDate.of(0,  1,  1);
  }


  @Override
  public LocalDate today() {
    return LocalDate.now();
  }
  
  
  @Override
  protected void validate(LocalDate date) throws UserEntryException {
    // Nothing more to validate
  }


  @Override
  protected LocalDate createFromYearMonthDay(int year, int month, int day) {
     return LocalDate.of(year, month, day);
  }


  @Override
  protected int[] splitDate(LocalDate date) {
    int[] components = new int[3];
    components[0] = date.getYear();
    components[1] = date.getMonthValue() - 1;
    components[2] = date.getDayOfMonth();
    return components;
  }
  
  
}

