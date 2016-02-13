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
package org.pennyledger.form.type;

import org.pennyledger.util.UserEntryException;
import org.pennyledger.value.FileContent;


public class FileContentType extends FileBasedType<FileContent> {
  
  public FileContentType () {
    super ();
  }
  
  
  public FileContentType (int viewSize, String dialogName, String[] filterExtensions, String[] filterNames) {
    super (viewSize, dialogName, filterExtensions, filterNames);
  }
  
  
  @Override
  public FileContent createFromString(String source, FileContent fillValue) throws UserEntryException {
    String sv = source.trim();
    FileContent fileContent = new FileContent(sv);
    validate(fileContent);
    return fileContent;
  }
  
  
  @Override
  public FileContent newInstance (String source) {
    return new FileContent(source);
  }
  
  
  @Override
  public FileContent primalValue() {
    return new FileContent(".");
  }



}
