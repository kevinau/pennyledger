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
package org.pennyledger.object.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.pennyledger.object.UserEntryException;

public interface IType<T> {
  
  public enum JsonType {
    ESCAPED_STRING,
    PLAIN_STRING,
    NUMBER,
    BOOLEAN;
  }
  
  public T createFromString (String source) throws UserEntryException;

  /**
   * Validate the source string and optionally create a new field object.  The return value will be <code>null</code> 
   * if the nullable parameter is true and the source string is empty.  Otherwise, an instance of the field object
   * will be returned.  An error is thrown if <code>null</code> or a valid field object cannot be created.
   */
  public T createFromString (T fillValue, boolean nullable, boolean creating, String source) throws UserEntryException;

  /** 
   * Create a new instance of the field object.  It is assumed that the source string is the string representation of
   * a valid field object.  An unchecked exception will be thrown if the a valid field object cannot be created, these
   * errors should not be checked for.
   */
  public T newInstance (String source);
  
  /**
   * Return the description of a value when the description is different from the entry value. Can return <code>null</code> if it does
   * not have a description that is distinct from its entry representation. If a
   * non-null value is returned, it should not duplicate the entry
   * representation.
   */
  public String toDescriptionString(T value);

  public String toEntryString (T value, T fillValue);

  public String toValueString (T value);
  
  public default JsonType getJsonType () {
    return JsonType.ESCAPED_STRING;
  }
  
  /**
   * A zero or empty field value.  For string values it will be the empty string.  For numeric values it will be zero.
   * Other fields will have an appropriate primal value.
   */
  public T primalValue ();

  public void validate (T value, boolean nullable) throws UserEntryException;

  public String getRequiredMessage();
  
  public int getFieldSize();

  public default boolean isPrimitive() {
    return false;
  }

  public default boolean isNullable() {
    return false;
  }
  
  
  public default Alignment getAlignment() {
    return Alignment.LEFT;
  }
  
  
  public default T newValue() {
    if (isNullable()) {
      return null;
    } else {
      return primalValue();
    }
  }

  /**
   * Returns the SQL type description for this type.
   */
  public String getSQLType();

  
  public default String[] getSQLTypes() {
    return null;
  }

  
  /**
   * Set an value, of this type, in a SQL statement.
   * @throws SQLException 
   */
  public void setSQLValue (PreparedStatement stmt, int sqlIndex, T value) throws SQLException;
    
    
  public default void setSQLValue (PreparedStatement stmt, int[] sqlIndex, T value) throws SQLException {
    setSQLValue (stmt, sqlIndex[0]++, value);
  }
  

  /**
   * Get a value from a SQL statement
   */
  public T getSQLValue (ResultSet resultSet, int sqlIndex) throws SQLException;
  
  
  public default T getSQLValue (ResultSet resultSet, int[] sqlIndex) throws SQLException {
    return getSQLValue (resultSet, sqlIndex[0]++);
  }

}
