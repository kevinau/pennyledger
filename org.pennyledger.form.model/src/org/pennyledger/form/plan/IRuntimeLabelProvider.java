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
package org.pennyledger.form.plan;


public interface IRuntimeLabelProvider {

  /**
   * Get a list of XPaths expressions that identify the fields that this plan
   * applies to. All matching fields will use the same getLabel method.
   * The list should never be empty, but there is no problem if it is. The
   * XPaths here are relative to the control which contains the
   * ILabelProviderMethod.
   * 
   * @return list of XPath expressions
   */
  public String[] getAppliesTo();

  /**
   * Get the label for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return the label for the designated fields.
   */
  public String getLabel();

}
