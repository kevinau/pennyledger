package org.pennyledger.report.page.pdf;


public class PDFBoolean extends PDFObject {

  private static final byte[] tx = {'t', 'r', 'u', 'e'};
  private static final byte[] fx = {'f', 'a', 'l', 's', 'e'};
  
  private boolean value;
  
  public PDFBoolean (String x) {
    value = new Boolean(x);
  }
  
  
  public PDFBoolean (boolean value) {
    this.value = value;
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    byte[] vx = value ? tx : fx;
    writer.write(vx, 0, vx.length);
  }
}
