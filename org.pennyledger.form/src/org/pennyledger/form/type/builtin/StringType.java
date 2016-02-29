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


import org.pennyledger.form.TextCase;
import org.pennyledger.util.UserEntryException;


public class StringType extends StringBasedType<String> {

  
  public StringType () {
    super ();
  }
  
  
  public StringType (int maxLength) {
    super (maxLength);
  }
  
  
  public StringType (int maxLength, TextCase allowedCase) {
    super (maxLength, allowedCase);
  }


  @Override
  protected String createFromString2(String source) throws UserEntryException {
    String value = newInstance(source);
    validate (value);
    return value;
  }


  @Override
  public String newInstance(String source) {
    return source;
  }


  @Override
  protected void validate(String value) throws UserEntryException {
  }
  
  
  @Override
  public String primalValue () {
    return "";
  }

}
