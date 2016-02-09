package org.pennyledger.html;

public class Attribute {
  private String name;
  private String value;

  Attribute(String name, String value) {
    this.name = name;
    this.value = value;
  }

  Attribute(String name) {
    this.name = name;
    this.value = null;
  }

  
  void write (StringBuilder b) {
    b.append(' ');
    b.append(name);
    if (value != null) {
      b.append('=');
      if (value.indexOf('"') == -1) {
        b.append('"');
        b.append(value);
        b.append('"');
      } else {
        b.append('\'');
        b.append(value);
        b.append('\'');
      }
    }
  }

}