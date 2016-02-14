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
import java.util.List;

import org.pennyledger.form.plan.IRuntimeDefaultProvider;


public class RuntimeDefaultProvider implements IRuntimeDefaultProvider {

  private final Method method;
  private final Object defaultValue;
  private final String[] appliesTo;
  private final String[] dependsOn;
  private final boolean isSlow;
  
//  private Object instance;
  
  
  public RuntimeDefaultProvider (Class<?> klass, FieldDependency fieldDependency, Method method, boolean isSlow, String[] appliesTo) {
    this.method = method;
    this.defaultValue = null;
    this.isSlow = isSlow;
    this.appliesTo = appliesTo;
    
    // Calculate dependencies
    List<String> dx = fieldDependency.getDependencies(klass.getName(), method.getName());
    this.dependsOn = dx.toArray(new String[dx.size()]);
  }

  
  public RuntimeDefaultProvider (Object defaultValue, String[] appliesTo) {
    this.method = null;
    this.defaultValue = defaultValue;
    this.isSlow = false;
    this.appliesTo = appliesTo;
    this.dependsOn = new String[0];
  }

  
  public RuntimeDefaultProvider (Class<?> klass, FieldDependency fieldDependency, Method method, boolean isSlow, String fieldName) {
    this (klass, fieldDependency, method, isSlow, new String[] {fieldName});
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
   * Get a list of field names that the getDefaultValue method depends on. Some
   * implementations may compute this from the code of the getDefaultValue method,
   * others will specify it explicitly.  The names here are relative to the control
   * which contains the IIntialValuePlan.
   * 
   * @return list of field names
   */
  @Override
  public String[] getDependsOn() {
    return dependsOn;
  }

  
  /**
   * Is the method to calculate the default value a slow process.  If it is, it
   * will be done within a background thread.
   */
  @Override
  public boolean isSlow() {
    return isSlow;
  }
  
  
  /**
   * Get the default value for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return the default value for the designated fields.
   */
  @Override
  public Object getDefaultValue(Object instance) {
    if (method == null) {
      return defaultValue;
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

  @Override
  public boolean isRuntime() {
    return method != null;
  }

}
