package org.pennyledger.report.page.pdf;


public class PDFName extends PDFObject{

  private String name;
  
  
  public PDFName (String name) {
    this.name = name;
  }
  
  
  public String getName () {
    return name;
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    writer.write('/');
    writer.write(name);
  }

  
  @Override
  public String toString () {
    return '/' + name;
  }
}
