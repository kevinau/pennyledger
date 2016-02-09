package org.pennyledger.html;

public class Style extends NodeWithChildren {

  Style () {
    super ("style");
  }
  
  
  public void add (String text) {
    super.addChild(new TextNode(text));
  }
  
}
