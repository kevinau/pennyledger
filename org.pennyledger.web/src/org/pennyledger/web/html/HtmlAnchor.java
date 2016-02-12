package org.pennyledger.web.html;

import java.net.URL;

public class HtmlAnchor extends HtmlElement {

  private final String href;
  private final String description;
  private String target;
  
  
  public HtmlAnchor (HtmlElement parentNode, String href) {
    super (parentNode, "a");
    this.href = href;
    this.description = null;
    this.target = null;
  }
  
  
  public HtmlAnchor (HtmlElement parentNode, URL href) {
    super (parentNode, "a");
    this.href = href.toString();
    this.description = null;
    this.target = null;
  }
  
  
  public HtmlAnchor (HtmlElement parentNode, String href, String description) {
    super (parentNode, "a");
    this.href = href;
    this.description = description;
    this.target = null;
  }
  
  
  public HtmlAnchor (HtmlElement parentNode, URL href, String description) {
    super (parentNode, "a");
    this.href = href.toString();
    this.description = description;
    this.target = null;
  }
  
  
  public void setTarget (String target) {
    this.target = target;
  }
  
  
  public HtmlImage newImage(String href, String description) {
    HtmlImage image = new HtmlImage(this, href);
    image.setTitle(description);
    return image;
  }
  
  
  public HtmlImage newImage(URL href, String description) {
    HtmlImage image = new HtmlImage(this, href);
    image.setTitle(description);
    return image;
  }
  
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "href", href);
    appendAttribute (builder, "title", description);
    appendAttribute (builder, "target", target);
  }
  
}
