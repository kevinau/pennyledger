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
package org.pennyledger.object.plan;


public interface IRuntimeLabelProvider extends IRuntimeProvider {

  /**
   * Get the label for the designated fields. The designated fields are
   * those listed by the getAppliesTo method.
   * 
   * @return the label for the designated fields.
   */
  public String getLabel(Object instance);

}
