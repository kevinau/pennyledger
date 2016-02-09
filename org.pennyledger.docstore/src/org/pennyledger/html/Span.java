package org.pennyledger.html;

public class Span extends NodeWithChildren {
  
  Span () {
    super ("span");
  }

  
  public void add(String text) {
    super.addChild(new TextNode(text));
  }
}
