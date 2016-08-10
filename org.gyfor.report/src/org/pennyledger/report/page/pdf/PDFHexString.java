package org.pennyledger.report.page.pdf;

public class PDFHexString extends PDFObject {

  private byte[] byteArray;
  
  
  public PDFHexString (byte[] byteArray) {
    this.byteArray = byteArray;
  }
  
  
  private void toHex(byte[] dest, byte[] source) {
    int j = 0;
    for (int i = 0; i < source.length; i++) {
      int x = source[i];
      int x0 = (x >> 4) & 0xF;
      if (x0 < 10) {
        dest[j++] = (byte)(x0 + '0');
      } else {
        dest[j++] = (byte)(x0 + 'a' - 10);
      }
      int x1 = (x & 0xF);
      if (x1 < 10) {
        dest[j++] = (byte)(x1 + '0');
      } else {
        dest[j++] = (byte)(x1 + 'a' - 10);
      }
    }
  }
  
  

  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    writer.write('<');
    writer.writeln();
    byte[] dest = new byte[byteArray.length * 2];
    toHex (dest, byteArray);
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16 * 6; j += 6) {
        writer.write(dest, i * 16 * 6 + j, 6);
        writer.write(' ');
      }
      writer.writeln();
    }
    writer.write('>');
    writer.writeln();
  }
  
}
