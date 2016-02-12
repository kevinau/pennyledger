package org.pennyledger.web.html;


public class HtmlLabel extends HtmlElement {

  private String labelFor;
  
  
  public HtmlLabel (HtmlElement parentNode) {
    super (parentNode, "label");
  }
  

  public HtmlLabel (HtmlElement parentNode, String labelFor) {
    super (parentNode, "label");
    setFor(labelFor);
  }
  

  public void setFor (String labelFor) {
    this.labelFor = labelFor;
  }
  
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "for", labelFor);
  }
  
}
