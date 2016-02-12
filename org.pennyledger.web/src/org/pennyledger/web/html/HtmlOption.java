package org.pennyledger.web.html;


public class HtmlOption extends HtmlElement {

  private Object value;
  
  public HtmlOption (HtmlSelect parentNode) {
    super (parentNode, "option");
  }

  public void setValue (Object value) {
    this.value = value;
  }
  
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "value", value.toString());
  }

}
