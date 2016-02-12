package org.pennyledger.web.html;




public class HtmlTable extends HtmlElement {
  
  public HtmlTable (HtmlElement parentNode) {
    super (parentNode, "table");
  }
  
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute(builder, "cellpadding", "0");
    appendAttribute(builder, "cellspacing", "0");
  }

  
  public HtmlTableRow newRow () {
    return new HtmlTableRow(this);
  }
  
}
