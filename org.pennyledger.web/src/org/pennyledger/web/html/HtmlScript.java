package org.pennyledger.web.html;


public class HtmlScript extends HtmlElement {

  private String src;
  
  
  public HtmlScript (HtmlElement parentNode) {
    super (parentNode, "script", true);
  }
  

  public HtmlScript (HtmlElement parentNode, String src) {
    super (parentNode, "script", true);
    this.src = src;
  }
  

  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "src", src);
  }
  
}
