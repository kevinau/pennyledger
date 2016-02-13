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


import org.pennyledger.form.TextCase;
import org.pennyledger.util.UserEntryException;


public class TextType extends StringBasedType<String> {

  
  public TextType () {
    super ();
  }
  
  
  public TextType (int maxSize) {
    super (maxSize);
  }
  
  
  public TextType (int maxSize, TextCase allowedCase) {
    super (maxSize, allowedCase);
  }


  @Override
  protected String createFromString2(String source, String fillValue) throws UserEntryException {
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
