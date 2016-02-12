package org.pennyledger.web.html;


public class HtmlHtml extends HtmlElement {

  private String lang;
  
  public HtmlHtml () {
    super (null, "html");
  }
  
  public HtmlHead newHead () {
    return new HtmlHead(this);
  }
  
  public HtmlBody newBody () {
    return new HtmlBody(this);
  }
  
  
  public void setLang (String lang) {
    this.lang = lang;
  }
  
  
  @Override 
  public void buildString (StringBuilder builder) {
    builder.append("<!doctype html>\r\n");
    builder.append("\r\n");
    buildString (builder, 0, false);
  }
  
  @Override
  protected void appendAttributes (StringBuilder builder) {
    appendAttribute(builder, "lang", lang);
  }

}
