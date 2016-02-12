package org.pennyledger.web.html;

import java.net.URL;


public class HtmlTableData extends HtmlElement {

  public HtmlTableData (HtmlTableRow parentNode) {
    super (parentNode, "td");
  }
  
  
  public HtmlAnchor newAnchor (URL href) {
    return new HtmlAnchor(this, href);
  }
  
  
  public HtmlAnchor newAnchor (String href) {
    return new HtmlAnchor(this, href);
  }
  
  
  public HtmlLabel newLabel (String lfor) {
    return new HtmlLabel(this, lfor);
  }

  
  public HtmlInput newInput (String type, String name) {
    return new HtmlInput(this, type, name);
  }
  
}
