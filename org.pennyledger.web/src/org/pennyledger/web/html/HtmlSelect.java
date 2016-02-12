package org.pennyledger.web.html;


public class HtmlSelect extends HtmlElement {

  private final String name;
  
  public HtmlSelect (HtmlElement parentNode, String name) {
    super (parentNode, "select");
    this.name = name;
    setId(name);
  }

  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "name", name);
  }

}
