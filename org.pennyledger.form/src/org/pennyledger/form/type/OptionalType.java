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


public class OptionalType<T> implements IType<T> {
    
  private final IType<T> nestedType;
  
  public OptionalType (IType<T> nestedType) {
    this.nestedType = nestedType;
  }
  
  
  public boolean isNullable () {
    return true;
  }
  

  @Override
  public T createFromString (String source, T fillType, boolean optional, boolean creating) throws UserEntryException {
    if (source.length() == 0) {
      return null;
    }
    return nestedType.createFromString(source, fillType, true, creating);
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
  public String getTypeName () {
    return "Optional" + nestedType.getTypeName();
  }


  @Override
  public boolean isPrimitive() {
    return false;
  }


  @Override
  public void setPrimitive(boolean primitive) {
    throw new RuntimeException("setPrimitive is not applicable here");
  }


  @Override
  public String toDescriptionString(T value) {
    if (value == null) {
      return "";
    }
    return nestedType.toDescriptionString(value);
  }
  
}

