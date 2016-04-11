package org.gyfor.report.page.pdf;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountedOutputStream extends BufferedOutputStream {

  private final byte[] newLine = System.getProperty("line.separator").getBytes();
  
  private int byteCount = 0;
  
  public CountedOutputStream (OutputStream out) {
    super (out);
  }

  
  public CountedOutputStream (OutputStream out, int size) {
    super (out, size);
  }
  
  
  @Override 
  public void write (byte[] buff, int offset, int length) {
    try {
      byteCount += length;
      super.write(buff, offset, length);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public void write (char c) {
    try {
      byteCount++;
      super.write((byte)c);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void write (int value) {
    String s = Integer.toString(value);
    write (s);
  }
  
  
  public void writeln (int value) {
    write (value);
    writeln ();
  }
  
  
  public void writeln (char c) {
    write (c);
    writeln ();
  }
  
  
  public void write (String s) {
    try {
      char[] sx = s.toCharArray();
      int n = sx.length;
      byte[] buffer = new byte[n];
      for (int i = 0; i < n; i++) {
        buffer[i] = (byte)sx[i];
      }
      byteCount += n;
      super.write(buffer, 0, n);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public void writeln (String s) {
    write(s);
    writeln();
  }
  
  
  public void write2n () {
    if (newLine.length == 1) {
      write(' ');
    }
    writeln();
  }
  
  
  public void writeln () {
    try {
      byteCount += newLine.length;
      super.write(newLine, 0, newLine.length);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  @Override
  public void close () {
    try {
      super.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }


  public int getOffset () {
    return byteCount;
  }
  
}
