package org.pennyledger.web.html;

public class HtmlTitle extends HtmlElement {

  private HtmlText title;
  
  public HtmlTitle (HtmlHead parentNode) {
    super (parentNode, "title");
  }
  
  public void addTitle (String title) {
    addText(title);
  }
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute(builder, "title", title);
  }

}
