package org.pennyledger.html;

public class TextNode extends Node {

  private final String text;
  
  TextNode (String text) {
    super (null);
    this.text = text;
  }
  
  
  @Override
  protected void write (StringBuilder b) {
    b.append(text);
  }
}
