package org.pennyledger.web.html;

public class HtmlButton extends HtmlElement {

  private String title;
  
  public HtmlButton (HtmlElement parentNode) {
    super (parentNode, "button");
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
