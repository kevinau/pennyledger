package org.gyfor.report.page.pdf;

public class PDFReference extends PDFObject {

  private final int objNumber;
  
  
  public PDFReference (int objNumber) {
    this.objNumber = objNumber;
  }
  
  
  public PDFReference (PDFIndirect indirect) {
    this.objNumber = indirect.getObjNumber();
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    writer.write(objNumber);
    writer.write(" 0 R ");
  }
  
}
