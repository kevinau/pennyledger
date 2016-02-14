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


public interface IRuntimeDefaultProvider {

  /**
   * Get a list of XPaths expressions that identify the fields that this plan
   * applies to. All matching fields will use the same getDefaultValue method.
   * The list should never be empty, but there is no problem if it is. The
   * XPaths here are relative to the control which contains the
   * IDefaultProviderMethod.
   * 
   * @return list of XPath expressions
   */
  public String[] getAppliesTo();

  /**
   * Get a list of field names that the getDefaultValue method depends on. Some
   * implementations may compute this from the code of the getDefaultValue
   * method, others will specify it explicitly. The names here are relative to
   * the control which contains the IDefaultProviderMethod.
   * 
   * @return list of field names
   */
  public String[] getDependsOn();

  /**
   * Is the method to calculate the default value a slow process. If it is, it
   * will be done within a background thread.
   */
  public boolean isSlow();

  /**
   * Get the default value for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return the default value for the designated fields.
   */
  public Object getDefaultValue(Object instance);

  /**
   * Does this provider return an initial mode, or does it only return a 
   * mode during runtime.  A provider that returns a mode at runtime requires
   * an instance value.
   * 
   * @return
   */
  public boolean isRuntime();

}
