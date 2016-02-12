package org.pennyledger.web.html;

import java.net.URL;


public class HtmlBody extends HtmlElement {

  public HtmlBody (HtmlHtml parentNode) {
    super (parentNode, "body");
  }
  
  
  public HtmlH1 newH1 () {
    return new HtmlH1(this);
  }
  
  
  public HtmlTable newTable () {
    return new HtmlTable(this);
  }
  
  
  public HtmlDiv newDiv () {
    return new HtmlDiv(this);
  }
  
  
  public HtmlImage newImage (String href) {
    return new HtmlImage(this, href);
  }
  
  
  public HtmlIFrame newIFrame (String src) {
    return new HtmlIFrame(this, src);
  }
  
  
  public HtmlAnchor newAnchor (String href) {
    return new HtmlAnchor(this, href);
  }
  
  public HtmlAnchor newAnchor (String href, String title) {
    return new HtmlAnchor(this, href, title);
  }
  
  public HtmlAnchor newAnchor (URL href, String title) {
    return new HtmlAnchor(this, href, title);
  }
  
}
