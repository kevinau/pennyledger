package org.gyfor.report.page.pdf;


public class PDFString extends PDFObject {

  private String value;
  
  
  public PDFString (String value) {
    this.value = value;
  }
  
  
  @Override
  public void write (int indent, CountedOutputStream writer) {
    StringBuilder sb = null;
    char[] vx = value.toCharArray();
    int i = 0;
    for (char c : vx) {
      if (c == '\\' || c == '(' || c == ')') {
        if (sb == null) {
          sb = new StringBuilder();
          sb.append('(');
          sb.append(vx, 0, i);
        }
        sb.append('\\');
        sb.append(c);
      } else {
        if (sb != null) {
          sb.append(c);
        } else {
          i++;
        }
      }
    }
    if (sb == null) {
      writer.write('(');
      writer.write(value);
      writer.write(')');
    } else {
      sb.append(')');
      writer.write(sb.toString());
    }
  }
}
