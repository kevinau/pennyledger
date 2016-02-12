package org.pennyledger.web.html;

public class HtmlDiv extends HtmlElement {

  public HtmlDiv (HtmlElement parentNode) {
    super (parentNode, "div");
  }
  
  
  public HtmlTable newTable() {
    return new HtmlTable(this);
  }

  
  public HtmlIFrame newIFrame (String src) {
    return new HtmlIFrame(this, src);
  }
  
}
