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


public interface IType<T> {
  
  /**
   * Validate the source string and optionally create a new field object.  The return value will be <code>null</code> 
   * if the optional parameter is true and the source string is empty.  Otherwise, an instance of the field object
   * will be returned.  An error is thrown if <code>null</code> or a valid field object cannot be created.
   */
  public T createFromString (String source, T fillValue, boolean optional, boolean creating) throws UserEntryException;

  public String getTypeName ();
  
  public boolean isPrimitive();
  
  /** 
   * Create a new instance of the field object.  It is assumed that the source string is the string representation of
   * a valid field object.  An unchecked exception will be thrown if the a valid field object cannot be created, these
   * errors should not be checked for.
   */
  public T newInstance (String source);
  
  /**
   * A zero or empty field value.  For string values it will be the empty string.  For numeric values it will be zero.
   * Other fields will have an appropriate primal value.
   */
  public T primalValue ();

  public void setPrimitive(boolean primitive);

  /**
   * Return the description of a value. Can return <code>null</code> if it does
   * not have a description that is distinct from its entry representation. If a
   * non-null value is returned, it should not duplicate the entry
   * representation.
   */
  public String toDescriptionString(T value);
  
  public String toEntryString (T value, T fillValue);
  
  public void validate (T value, boolean optional) throws UserEntryException;

}
