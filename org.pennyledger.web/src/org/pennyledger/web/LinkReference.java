package org.pennyledger.web;

import java.io.File;
import java.io.PrintWriter;

public class LinkReference {

  private final String rel; 
  private final File href;
  private final String type;
  
  public LinkReference (String rel, File href, String type) {
    this.rel = rel;
    this.href = href;
    this.type = type;
  }
  
  
  public LinkReference (String rel, File href) {
    this(rel, href, null);
  }
  
  
  File getHref () {
    return href;
  }
  
  
  public void output (PrintWriter writer) {
    writer.append("<link rel='");
    writer.append(rel);
    writer.append("' href='");
    writer.append(href.toString());
    writer.append("'");
    if (type != null) {
      writer.append(" type='");
      writer.append(type);
      writer.append("'");
    }
    writer.append(" />");
  }
}
