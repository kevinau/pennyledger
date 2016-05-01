package org.gyfor.report.page.pdf;


public abstract class PDFObject {
  
  public abstract void write (int indent, CountedOutputStream writer);

  public void write (CountedOutputStream writer) {
    write (0, writer);
  }
  
  
  protected void spaceIn (int indent, CountedOutputStream writer) {
//    for (int i = 0; i < indent; i++) {
//      writer.write(' ');
//    }
  }
}
