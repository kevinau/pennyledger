package org.pennyledger.docstore.parser.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SafeOutputStream extends OutputStream {

  private final Path targetPath;
  private final File tempFile;
  private final OutputStream tempOutputStream;
  private boolean isClosed;
  
  public SafeOutputStream (Path targetPath) throws FileNotFoundException {
    this.targetPath = targetPath;
    this.tempFile = new File(targetPath.toString() + ".tmp");
    this.tempFile.getParentFile().mkdirs();
    this.tempOutputStream = new FileOutputStream(tempFile);
    tempFile.deleteOnExit();
    isClosed = false;
  }
  
  
  @Override
  public void write(byte[] b) throws IOException {
    tempOutputStream.write(b);
  }


  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    tempOutputStream.write(b, off, len);
  }


  @Override
  public void write(int b) throws IOException {
    tempOutputStream.write(b);
  }
  
  
  @Override
  public void flush() throws IOException {
    tempOutputStream.flush();
  }
  
  
  @Override
  public void close() throws IOException {
    if (!isClosed) {
      isClosed = true;
      tempOutputStream.close();
      Files.move(tempFile.toPath(),  targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }
  }
  
}
