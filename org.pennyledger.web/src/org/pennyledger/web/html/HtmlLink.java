package org.pennyledger.web.html;


public class HtmlLink extends HtmlElement {

  private String rel;
  private String href;
  
  
  public HtmlLink (HtmlElement parentNode) {
    super (parentNode, "link", false);
  }
  

  public HtmlLink (HtmlElement parentNode, String rel, String href) {
    super (parentNode, "link", false);
    this.rel = rel;
    this.href = href;
  }
  

  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "rel", rel);
    appendAttribute (builder, "href", href);
  }
  
}
