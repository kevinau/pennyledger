package org.pennyledger.html;

public class Div extends NodeWithChildren {
  
  Div () {
    super ("div");
  }


  public Span newSpan() {
    Span span = new Span();
    super.addChild(span);
    return span;
  }

  
  public void add(String text) {
    super.addChild(new TextNode(text));
  }
}
