package org.gyfor.report.page.pdf;


public class PDFStream extends PDFObject {

  private PDFDictionary dict;
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    dict.write(indent, writer);
    writer.writeln();
    spaceIn (indent, writer);
    writer.writeln("stream");
    spaceIn (indent, writer);
    writer.writeln("endstream");
  }
}
