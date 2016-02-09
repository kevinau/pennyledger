package org.pennyledger.html;

import java.util.HashMap;
import java.util.Map;

public abstract class Node {

  protected String tag;
  protected Map<String, String> attributes = null;

  
  protected Node (String tag) {
    this.tag = tag;
  }

  
  protected void setAttribute(String name, String value) {
    if (attributes == null) {
      attributes = new HashMap<>(10);
    }
    attributes.put(name, value);
  }

  
  @SuppressWarnings("unchecked")
  public <X extends Node> X setId(String value) {
    setAttribute("id", value);
    return (X)this;
  }

  
  @SuppressWarnings("unchecked")
  public <X extends Node> X setClass(String value) {
    setAttribute("class", value);
    return (X)this;
  }


  @SuppressWarnings("unchecked")
  public <X extends Node> X setTitle(String value) {
    setAttribute("title", value);
    return (X)this;
  }


  protected void write(StringBuilder b) {
    b.append('<');
    b.append(tag);
    if (attributes != null) {
      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        b.append(' ');
        b.append(entry.getKey());
        String value = entry.getValue();
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
    b.append('>');
  }


  protected void writeEndTag(StringBuilder b) {
    b.append('<');
    b.append('/');
    b.append(tag);
    b.append('>');
  }

}
