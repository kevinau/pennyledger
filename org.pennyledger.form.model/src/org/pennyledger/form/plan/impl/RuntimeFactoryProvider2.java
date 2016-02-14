/*******************************************************************************
 * Copyright (c) 2008 Kevin Holloway (kholloway@geckosoftware.com.au).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.txt
 * 
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 ******************************************************************************/
package org.pennyledger.form.plan.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.pennyledger.form.plan.IRuntimeFactoryProvider2;



public class RuntimeFactoryProvider2 implements IRuntimeFactoryProvider2 {

  private final Method method;
  private final Object independentNewValue;
  private final String[] appliesTo;
  
  
  public RuntimeFactoryProvider2 (Method method, String[] appliesTo) {
    this.method = method;
    this.independentNewValue = null;
    this.appliesTo = appliesTo;
  }

  
  public RuntimeFactoryProvider2 (Object independentNewValue, String[] appliesTo) {
    this.method = null;
    this.independentNewValue = independentNewValue;
    this.appliesTo = appliesTo;
  }

  
  public RuntimeFactoryProvider2 (Method method, String fieldName) {
    this (method, new String[] {fieldName});
  }
  
  
  /**
   * Get a list of XPaths expressions that identify the fields that this plan
   * applies to. All matching fields will use the same getDefaultValue method.
   * The list should never be empty, but there is no problem if it is. The
   * XPaths here are relative to the control which contains the
   * IDefaultProviderMethod.
   * 
   * @return list of XPath expressions
   */
  @Override
  public String[] getAppliesTo() {
    return appliesTo;
  }

  /**
   * Create a new value for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return a new value for the designated fields.
   */
  @Override
  public Object createNewValue(Object instance) {
    if (method == null) {
      return independentNewValue;
    } else {
      if (instance == null) {
        throw new IllegalArgumentException();
      }
      try {
        method.setAccessible(true);
        return method.invoke(instance);
      } catch (SecurityException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      } catch (InvocationTargetException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

}
