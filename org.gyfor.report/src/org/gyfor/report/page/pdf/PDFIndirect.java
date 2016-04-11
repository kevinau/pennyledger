package org.gyfor.report.page.pdf;

import java.util.zip.Deflater;


public class PDFIndirect extends PDFDictionary {

  private int objNumber;
  private ByteArrayBuilder byteArray = null;
  
  
  public PDFIndirect (PDFDocument document) {
    objNumber = document.getBodyObjectCount() + 1;
    document.addBodyObject(this);
  }

  
  public PDFIndirect (PDFDocument document, String type) {
    this (document);
    put ("Type", new PDFName(type));
  }

  
  public int getObjNumber () {
    return objNumber;
  }
  
  
  private final PDFObject[] filterList = {
      new PDFName("FlateDecode"),
  };
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    byte[] output = null;
    int compressedLength = 0;
    
    if (byteArray != null) {
//      output = new byte[byteArray.size() + 15];
//      Deflater compressor = new Deflater();
//      compressor.setInput(byteArray.array());
//      compressor.finish();
//      compressedLength = compressor.deflate(output);
//      put("Filter", filterList);
//      put("Length", compressedLength);
      put("Length", byteArray.size());
    }
    
    writer.write(objNumber);
    writer.writeln(" 0 obj");
    spaceIn (2, writer);
    super.write(2, writer);
    writer.writeln();
    if (byteArray != null) {
      writer.write("stream\r\n");
      writer.write(byteArray.array(), 0, byteArray.size());
      //writer.write(output, 0, compressedLength);
      writer.writeln();
      writer.writeln("endstream");
      byteArray = null;
    }
    writer.writeln("endobj");
  }

  
  public ByteArrayBuilder getByteArray () {
    if (byteArray == null) {
      byteArray = new ByteArrayBuilder();
    }
    return byteArray;
  }
  
  
  public void setLength () {
    if (byteArray != null) {
      put("Length", byteArray.size());
    }
  }

  
  public PDFReference getReference () {
    return new PDFReference(objNumber);
  }
  
}
