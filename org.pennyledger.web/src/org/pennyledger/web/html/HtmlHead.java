package org.pennyledger.web.html;

import java.net.URL;


public class HtmlHead extends HtmlElement {

  public HtmlHead (HtmlHtml parentNode) {
    super (parentNode, "head");
  }
  
  
  public HtmlTitle newTitle () {
    return new HtmlTitle(this);
  }
  
  public HtmlTitle newTitle (String t) {
    HtmlTitle title = new HtmlTitle(this);
    title.addText(t);
    return title;
  }
  
  public HtmlScript newScript (String src) {
    return new HtmlScript(this, src);
  }
  
  public HtmlStyle newStyle () {
    return new HtmlStyle(this);
  }
  
  public HtmlLink newLink (String rel, String href) {
    return new HtmlLink(this, rel, href);
  }
  
  public HtmlLink newLink (String rel, URL url) {
    return new HtmlLink(this, rel, url.toString());
  }
  
}
