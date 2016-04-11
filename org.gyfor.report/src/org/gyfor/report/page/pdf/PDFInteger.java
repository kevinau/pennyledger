package org.gyfor.report.page.pdf;


public class PDFInteger extends PDFObject {

  private final int value;
  
  public PDFInteger (int value) {
    this.value = value;
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    writer.write(value);
  }
 
}
