package org.pennyledger.web.html;


public class HtmlSpan extends HtmlElement {

  private String title;
  
  public HtmlSpan (HtmlElement parentNode) {
    super (parentNode, "span");
  }
  
  @Override
  public void setTitle (String title) {
    this.title = title;
  }
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute(builder, "title", title);
  }

}
