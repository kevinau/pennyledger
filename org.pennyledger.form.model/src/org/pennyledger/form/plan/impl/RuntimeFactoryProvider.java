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
import java.util.Arrays;
import java.util.List;

import org.pennyledger.form.plan.IRuntimeFactoryProvider;



public class RuntimeFactoryProvider implements IRuntimeFactoryProvider {

  private final Method method;
  private final Object independentNewValue;
  private final String[] appliesTo;
  private final String[] dependsOn;
  
  
  public RuntimeFactoryProvider (Class<?> klass, FieldDependency fieldDependency, Method method, String[] appliesTo) {
    this.method = method;
    this.independentNewValue = null;
    this.appliesTo = appliesTo;
    
    // Calculate dependencies
    List<String> dx = fieldDependency.getDependencies(klass.getName(), method.getName());
    
    // Remove the appliesTo from the dependency list
    for (String x : appliesTo) {
      dx.remove(x);
    }
    this.dependsOn = dx.toArray(new String[dx.size()]);
  }

  
  public RuntimeFactoryProvider (Object independentNewValue, String[] appliesTo) {
    this.method = null;
    this.independentNewValue = independentNewValue;
    this.appliesTo = appliesTo;
    this.dependsOn = new String[0];
  }

  
  public RuntimeFactoryProvider (Class<?> klass, FieldDependency fieldDependency, Method method, String fieldName) {
    this (klass, fieldDependency, method, new String[] {fieldName});
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

  @Override
  public boolean isRuntime() {
    return method != null && dependsOn.length > 0;
  }


  @Override
  public String toString() {
    return "RuntimeFactoryProvider[method=" + method + ", independentNewValue=" + independentNewValue + ", appliesTo=" + Arrays.toString(appliesTo) + ", dependsOn=" + Arrays.toString(dependsOn) + "]";
  }

  

}
