package org.gyfor.report.page.pdf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.gyfor.report.PaperSize;

public class PDFDictionary extends PDFObject {

  private Map<String, PDFObject> map = new LinkedHashMap<String, PDFObject>();
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    writer.writeln("<<");
    for (Map.Entry<String, PDFObject> entry : map.entrySet()) {
      spaceIn (indent + 3, writer);
      writer.write('/');
      writer.write(entry.getKey());
      writer.write(' ');
      entry.getValue().write(indent + 3, writer);
      writer.writeln();
    }
    spaceIn (indent, writer);
    writer.write(">>");
  }
  
  
  public void put (String name, List<? extends PDFObject> objList) {
    map.put(name, new PDFArray(objList));
  }
  
  
  public void put (String name, PDFObject[] objArray) {
    map.put(name, new PDFArray(objArray));
  }
  
  
  public void put (String name, PDFIndirect indirect) {
    map.put(name, indirect.getReference());
  }
  
  
  public void put (String name, PDFObject object) {
    map.put(name, object);
  }
  
  
  public void put (String name, int value) {
    map.put(name, new PDFInteger(value));
  }
  
  
  public void put (String name, double value) {
    map.put(name, new PDFDecimal(value));
  }
  
  public void put (String name, PaperSize pageSize) {
    PDFObject[] bounds = new PDFDecimal[] {
        new PDFDecimal(0),
        new PDFDecimal(0),
        new PDFDecimal(pageSize.getWidth()),
        new PDFDecimal(pageSize.getHeight()),
    };
    map.put(name, new PDFArray(bounds));
  }
  
  
  public PDFObject get (String name) {
    return map.get(name);
  }
  
}
