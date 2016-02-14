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

import org.pennyledger.form.type.IType;
import org.pennyledger.form.plan.IRuntimeTypeProvider;



public class RuntimeTypeProvider implements IRuntimeTypeProvider {

  private final IType<?> type;
  private final String[] appliesTo;
  
  
  public RuntimeTypeProvider (IType<?> type, String[] appliesTo) {
    this.type = type;
    this.appliesTo = appliesTo;
  }

  
  /**
   * Get a list of XPaths expressions that identify the fields that this plan
   * applies to. All matching fields will use the same getType method.
   * The list should never be empty, but there is no problem if it is. The
   * XPaths here are relative to the control which contains the
   * ITypeProviderMethod.
   * 
   * @return list of XPath expressions
   */
  @Override
  public String[] getAppliesTo() {
    return appliesTo;
  }

  /**
   * Get the default value for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return the default value for the designated fields.
   */
  @Override
  public IType<?> getType() {
    return type;
  }

}
