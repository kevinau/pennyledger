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

import org.j2form.type.IType;
import org.pennyledger.util.UserEntryException;

public abstract class Type<T> implements IType<T> {

  @Override
  public abstract T createFromString (String source) throws UserEntryException;
  
  
  @Override
  public T createFromString (T fillValue, boolean optional, boolean creating, String source) throws UserEntryException {
    source = source.trim();
    if (source.length() == 0) {
      if (optional) {
        return null;
      } else {
        throw new UserEntryException(getRequiredMessage(), UserEntryException.Type.REQUIRED);
      }
    }
    return createFromString(fillValue, source);
  }
    
  
  protected T createFromString (T fillValue, String source) throws UserEntryException {
    return createFromString(source);
  }
  
  
  @Override
  public abstract String getRequiredMessage (); 
  
  
  @Override 
  public abstract T primalValue ();
  
  
  @Override
  public abstract T newInstance(String source);
    
  
  @Override
  public String toDescriptionString(T value) {
    return null;
  }
  
  
  @Override
  public String toEntryString(T value, T fillValue) {
    if (value == null) {
      return "";
    }
    return value.toString();
  }
  
  
  protected abstract void validate(T value) throws UserEntryException;

  
  @Override
  public void validate(T value, boolean optional) throws UserEntryException {
    if (value == null) {
      if (optional) {
        return;
      } else {
        throw new UserEntryException(getRequiredMessage());
      }
    }
    validate(value);
  }

}
