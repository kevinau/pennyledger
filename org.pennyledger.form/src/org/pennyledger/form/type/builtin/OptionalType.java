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

import org.pennyledger.form.type.IType;
import org.pennyledger.util.UserEntryException;

public class OptionalType<T> implements IType<T> {
    
  private final IType<T> nestedType;
  
  public OptionalType (IType<T> nestedType) {
    this.nestedType = nestedType;
  }
  
  
  @Override
  public T createFromString (String source) throws UserEntryException {
    return nestedType.createFromString(source);
  }
  
  @Override
  public T createFromString (T fillType, boolean optional, boolean creating, String source) throws UserEntryException {
    if (source.length() == 0) {
      return null;
    }
    return nestedType.createFromString(fillType, true, creating, source);
  }
  
  
  @Override
  public void validate(T value, boolean optional) throws UserEntryException {
    if (value == null) {
      return;
    }
    nestedType.validate(value, true);
  }


  @Override
  public String toEntryString (T value, T fillValue) {
    if (value == null) {
      return "";
    }
    return nestedType.toEntryString(value, fillValue);
  }
  
  
  @Override 
  public T primalValue () {
    return nestedType.primalValue();
  }


  @Override
  public T newInstance(String source) {
    if (source.length() == 0) {
      return null;
    }
    return nestedType.newInstance(source);
  }
  
  
  @Override
  public String toDescriptionString(T value) {
    if (value == null) {
      return "";
    }
    return nestedType.toDescriptionString(value);
  }


  @Override
  public String getRequiredMessage() {
    throw new RuntimeException("getRequiredMessage is not applicable here");
  }


  @Override
  public int getFieldSize() {
    return nestedType.getFieldSize();
  }


  @Override
  public boolean isPrimitive() {
    return false;
  }
  
}

