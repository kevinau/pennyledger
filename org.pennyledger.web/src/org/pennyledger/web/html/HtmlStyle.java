package org.pennyledger.web.html;


public class HtmlStyle extends HtmlElement {

  public HtmlStyle (HtmlElement parentNode) {
    super (parentNode, "style", true);
  }

  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
  }
  
}
