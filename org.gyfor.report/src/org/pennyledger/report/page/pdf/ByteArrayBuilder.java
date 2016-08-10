package org.pennyledger.report.page.pdf;

/**
 * @author Kevin Holloway
 *
 */
public class ByteArrayBuilder {

  private byte[] buffer;
  private int index;
  
  public ByteArrayBuilder (int allocation) {
    buffer = new byte[allocation];
    index = 0;
  }
  
  
  public ByteArrayBuilder () {
    this (512);
  }
  
  
  private void extendBuffer () {
    byte[] buffer2 = new byte[buffer.length * 2];
    System.arraycopy(buffer, 0, buffer2, 0, buffer.length);
    buffer = buffer2;
  }
  
  
  public void append (byte b) {
    if (index + 1 >= buffer.length) {
      extendBuffer();
    }
    buffer[index++] = b;
  }
  
  
  public void append (byte[] b, int start, int length) {
    while (index + length >= buffer.length) {
      extendBuffer();
    }
    System.arraycopy(b, start, buffer, index, length);
    index += length;
  }
  
  
  public void append (byte[] b) {
    append (b, 0, b.length);
  }
  
  
  public void append (char c) {
    if (index + 1 >= buffer.length) {
      extendBuffer();
    }
    buffer[index++] = (byte)c;
  }
  
  
  public void append (int i) {
    append(Integer.toString(i));
  }

  
  public void append (float f) {
    append(Float.toString(f));
  }

  
  public void append (double d) {
    append ((float)d);
  }

  
  public void append (String s) {
    char[] sx = s.toCharArray();
    while (index + sx.length >= buffer.length) {
      extendBuffer();
    }
    for (char c : sx) {
      buffer[index++] = (byte)c;
    }
  }
  
  
  public void appendSpace () {
    if (index + 1 >= buffer.length) {
      extendBuffer();
    }
    buffer[index++] = ' ';
  }

  
  public byte[] array () {
    return buffer;
  }
  
  
  public int size () {
    return index;
  }
  
  
  public void reset () {
    index = 0;
  }
  
  
  public int getIndex () {
    return index;
  }

  
  @Override
  public String toString () {
    return new String(buffer, 0, index);
  }
  
  
  public static void main (String[] args) {
    ByteArrayBuilder builder = new ByteArrayBuilder(8);
    for (int i = 0; i < 26; i++) {
      builder.append((byte)(i + 'a'));
    }
    System.out.println(new String(builder.array(), 0, builder.size()));
  }
}
