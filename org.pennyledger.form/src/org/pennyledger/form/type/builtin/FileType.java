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
package org.pennyledger.form.type.builtin;

import java.io.File;

import org.pennyledger.util.UserEntryException;


public class FileType extends FileBasedType<File> {

  public FileType () {
    super ();
  }
  
  
  public FileType (int viewSize, String dialogName, String[] filterExtensions, String[] filterNames) {
    super (viewSize, dialogName, filterExtensions, filterNames);
  }


  @Override
  public File createFromString (String source) throws UserEntryException {
    String sv = source.trim();
    File file = new File(sv);
    validate (file);
    return file;
  }


  @Override
  public File primalValue() {
    String userHome = System.getProperty("user.home");
    return new File(userHome);
  }


  @Override
  public File newInstance(String source) {
    return new File(source);
  }

  
  @Override
  public int getFieldSize() {
    return 255;
  }

}
