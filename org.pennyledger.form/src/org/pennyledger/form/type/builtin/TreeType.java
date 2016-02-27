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


import java.util.List;

import org.pennyledger.value.ICodeValue;


public class TreeType extends CodeBasedType<ICodeValue> {

  public TreeType () {
    this (null, null);
  }
  
  
  public TreeType (List<ICodeValue> valueList) {
    this (valueList, null, null);
  }
  
  
  public TreeType (String shortName, String longName) {
    this (null, shortName, longName);
  }
  
  
  public TreeType (List<ICodeValue> valueList, String shortName, String longName) {
    super (valueList, shortName, longName);
  }
  
}
