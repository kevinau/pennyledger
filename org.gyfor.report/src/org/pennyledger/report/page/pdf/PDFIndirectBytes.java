package org.pennyledger.report.page.pdf;

public class PDFIndirectBytes extends PDFIndirect {

  public PDFIndirectBytes (PDFDocument document) {
    super (document);
  }

  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    ByteArrayBuilder byteArray = getByteArray();
    //if (byteArray != null) {
    //  put("Length", byteArray.size());
    //}
    
    writer.write(getObjNumber());
    writer.writeln(" 0 obj");
    //spaceIn (2, writer);
    //writeDictionary(2, writer);
    //writer.writeln();
    if (byteArray != null) {
      //writer.write("stream\r\n");
      writer.write(byteArray.array(), 0, byteArray.size());
      writer.writeln();
      //writer.writeln("endstream");
      byteArray = null;
    }
    writer.writeln("endobj");
  }

}
