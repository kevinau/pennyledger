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

import org.pennyledger.form.EntryMode;


public interface IRuntimeModeProvider {

  /**
   * Get a list of field names that this field use provider applies to. All fields listed
   * here will use the same getFieldUse method. The list should never be empty, but
   * there is no problem if it is.  The names here are relative to the control
   * which contains the runtime mode provider.
   * 
   * @return list of field names
   */
  public String[] getAppliesTo();

  /**
   * Get a list of field names that the getFieldUse method depends on. Some
   * implementations may compute this from the code of the getFieldUse method,
   * others will specify it explicitly.  The names here are relative to the control
   * which contains the runtime mode provider.
   * 
   * @return list of field names
   */
  public String[] getDependsOn();

  
  public EntryMode getEntryMode(Object instance);

  
  /**
   * Does this provider return an initial mode, or does it only return a 
   * mode during runtime.  A provider that returns a mode at runtime requires
   * an instance value.
   * 
   * @return
   */
  public boolean isRuntime ();
}
