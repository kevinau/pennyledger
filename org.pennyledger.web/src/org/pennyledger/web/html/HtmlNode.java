package org.pennyledger.web.html;

public abstract class HtmlNode {

  @Override 
  public String toString () {
    StringBuilder builder = new StringBuilder();
    buildString (builder);
    return builder.toString();
  }
  
  
  public abstract void buildString(StringBuilder builder);
  
  public abstract void buildString(StringBuilder builder, int level, boolean pack);
  
}
