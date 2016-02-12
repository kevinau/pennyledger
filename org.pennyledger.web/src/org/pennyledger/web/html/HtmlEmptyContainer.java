package org.pennyledger.web.html;

public class HtmlEmptyContainer extends HtmlElement {

  public HtmlEmptyContainer (HtmlElement parentNode) {
    super (parentNode, null);
  }
  

  @Override 
  public String toString () {
    StringBuilder builder = new StringBuilder();
    buildTopLevelString (builder);
    return builder.toString();
  }
  
}
