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
package org.pennyledger.object.value;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;

public class FileContent extends File implements Serializable {

  private static final long serialVersionUID = 1L;

  private byte[] contents;
  
  public FileContent (String fileName) {
    super (fileName);
  }
  
  
  public FileContent (File file) {
    super (file.toString());
    loadFile(file);
  }
  
  
  public FileContent (String fileName, byte[] contents) {
    super (fileName);
    this.contents = contents;
  }
  
  
  private void loadFile (File file) {
    if (file.exists() && file.isFile() && file.canRead()) {
      int n = (int)file.length();
      contents = new byte[n];
      try {
        InputStream is = new FileInputStream(file);
        is.read(contents);
        is.close();
      } catch (FileNotFoundException ex) {
        throw new RuntimeException(ex);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
  
  
  @Override
  public long length() {
    return contents.length;
  }
  
  
  public byte[] getContents () {
    return contents;
  }
  
  
  public File save (File destDir) {
    File outputFile = new File(destDir, super.getName());
    try {
      OutputStream outputStream = new FileOutputStream(outputFile);
      outputStream.write(contents);
      outputStream.close();
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    return outputFile;
  }
  
  
  public InputStream getInputStream () {
    return new ByteArrayInputStream(contents);
  }
  
  
  public Reader getReader () {
    return new InputStreamReader(new ByteArrayInputStream(contents));
  }


  public String getFileName() {
    return super.getName();
  }
  
}
