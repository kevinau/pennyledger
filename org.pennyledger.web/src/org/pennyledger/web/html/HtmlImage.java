package org.pennyledger.web.html;

import java.net.URL;

public class HtmlImage extends HtmlElement {

  private final String href;
  
  private Integer width;
  private Integer height;
  
  
  public HtmlImage(HtmlElement parentNode, URL href) {
    super (parentNode, "img", false);
    this.href = href.toString();
  }
  
  
  public HtmlImage(HtmlElement parentNode, String href) {
    super (parentNode, "img", false);
    this.href = href;
  }
  
  
  public void setSize (int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    super.appendAttributes(builder);
    appendAttribute (builder, "src", href);
    appendAttribute (builder, "width", width);
    appendAttribute (builder, "height", height);
  }
}
  
