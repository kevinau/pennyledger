package org.pennyledger.report.page.pdf;

import java.util.Arrays;
import java.util.List;

public class PDFArray extends PDFObject {

  private List<? extends PDFObject> objList;
  
  
  public PDFArray (List<? extends PDFObject> objList) {
    this.objList = objList;
  }
  
  
  public PDFArray (PDFObject[] objArray) {
    this.objList = Arrays.asList(objArray);
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    boolean complex = false;
    for (PDFObject v : objList) {
      if (v instanceof PDFArray || v instanceof PDFDictionary) {
        complex = true;
      }
    }
    if (complex) {
      writer.writeln('[');
      for (PDFObject v : objList) {
        spaceIn (indent + 2, writer);
        v.write (indent + 2, writer);
        writer.writeln();
      }
      spaceIn (indent, writer);
      writer.write(']');
    } else {
      boolean first = true;
      writer.write('[');
      for (PDFObject v : objList) {
        if (first) {
          first = false;
        } else {
          writer.write(' ');
        }
        v.write (indent, writer);
      }
      writer.write(']');
    }
  }
}
