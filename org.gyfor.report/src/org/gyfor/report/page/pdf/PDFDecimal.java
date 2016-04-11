package org.gyfor.report.page.pdf;


public class PDFDecimal extends PDFObject {

  private final double value;
  
  public PDFDecimal (double value) {
    this.value = value;
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    String x = Double.toString(value);
    writer.write(x);
  }
 
}
