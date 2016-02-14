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

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.plan.IRuntimeModeProvider;


public class RuntimeModeProvider implements IRuntimeModeProvider {

  private final Method method;
  private final EntryMode mode;
  private final String[] appliesTo;
  private final String[] dependsOn;
  
  
  public RuntimeModeProvider (Class<?> klass, FieldDependency fieldDependency, Method method, String[] appliesTo) {
    this.method = method;
    this.mode = null;
    this.appliesTo = appliesTo;
    
    // Calculate dependencies
    List<String> dx = fieldDependency.getDependencies(klass.getName(), method.getName());
    this.dependsOn = dx.toArray(new String[dx.size()]);
  }

  
  public RuntimeModeProvider (EntryMode mode, String[] appliesTo) {
    this.method = null;
    this.mode = mode;
    this.appliesTo = appliesTo;
    this.dependsOn = new String[0];
  }

  
  public RuntimeModeProvider (Class<?> klass, FieldDependency fieldDependency, Method method, String fieldName) {
    this (klass, fieldDependency, method, new String[] {fieldName});
  }
  
  
  /**
   * Get a list of field names that this field use plan applies to. All fields listed
   * here will use the same getFieldUse method. The list should never be empty, but
   * there is no problem if it is.  The names here are relative to the control
   * which contains the IRuntimeUseProvider.
   * 
   * @return list of field names
   */
  @Override
  public String[] getAppliesTo() {
    return appliesTo;
  }

  /**
   * Get a list of field names that the getFieldUse method depends on. Some
   * implementations may compute this from the code of the getFieldUse method,
   * others will specify it explicitly.  The names here are relative to the control
   * which contains the IRuntimeUseProvider.
   * 
   * @return list of field names
   */
  @Override
  public String[] getDependsOn() {
    return dependsOn;
  }

  
  /**
   * Get the field use for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return the field use for the designated fields.
   */
  @Override
  public EntryMode getEntryMode(Object instance) {
    if (method != null) {
      try {
        method.setAccessible(true);
        return (EntryMode)method.invoke(instance);
      } catch (SecurityException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalArgumentException ex) {
        throw new RuntimeException(ex);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      } catch (InvocationTargetException ex) {
        throw new RuntimeException(ex);
      }
    } else {
      return mode;
    }
  }
  
  
  @Override 
  public String toString () {
    StringBuilder s = new StringBuilder();
    s.append("RuntimeModeProvider(");
    s.append(mode);
    s.append(',');
    if (method != null) {
      s.append(method.getName());
    }
    s.append(",[");
    for (int i = 0; i < appliesTo.length; i++) {
      if (i > 0) s.append(",");
      s.append(appliesTo[i].toString());
    }
    s.append("],[");
    for (int i = 0; i < dependsOn.length; i++) {
      if (i > 0) s.append(",");
      s.append(dependsOn[i]);
    }
    s.append("])");
    return s.toString();
  }


  @Override
  public boolean isRuntime() {
    return method != null && dependsOn.length > 0;
  }

}
