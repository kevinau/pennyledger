package org.pennyledger.web.html;


public class HtmlIFrame extends HtmlElement {

  private String src;
  
  public HtmlIFrame (HtmlElement parentNode) {
    super (parentNode, "iframe", false);
  }
  

  public HtmlIFrame (HtmlElement parentNode, String src) {
    super (parentNode, "iframe", false);
    this.src = src;
  }
  

  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "src", src);
  }
  
}
