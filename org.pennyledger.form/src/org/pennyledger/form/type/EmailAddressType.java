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


public class EmailAddressType extends RegexTextType {
    
  private static final String regex = "^[a-zA-Z0-9_\\-\\.]+@([a-zA-Z0-9_\\-]+\\.)+[a-zA-Z]{2,7}$";
  
  
  public EmailAddressType () {
    super (128, regex, "not a valid email address");
  }
  
  
  public int getViewSize() {
    return 40;
  }
  
}
