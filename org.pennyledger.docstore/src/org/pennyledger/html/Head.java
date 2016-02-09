package org.pennyledger.html;

public class Head extends NodeWithChildren {

  Head() {
    super("head");
  }


  public Style newStyle () {
    Style style = new Style();
    addChild (style);
    return style;
  }

}
