package org.pennyledger.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class HtmlPage {

  private String title;
  
  private List<LinkReference> linkRefs = new ArrayList<>();
  
  private List<String> styleContents = new ArrayList<>();
  
  private StringBuilder bodyContent = new StringBuilder();
  
  private HttpServletResponse response;
  
  private boolean closed = false;
  
  
  public HtmlPage (HttpServletResponse response) {
    this.response = response;
  }
  

  public void setTitle (String title) {
    this.title = title;
  }
  
  
  String getTitle () {
    return title;
  }
  
  
  public void close () {
    this.closed = true;
  }
  
  
  public boolean isClosed () {
    return closed;
  }
  
  
  public HttpServletResponse getResponse () {
    return response;
  }
  
  
  public void addLink (String rel, File styleLink, String type) {
    for (LinkReference link : linkRefs) {
      if (link.getHref().equals(styleLink)) {
        return;
      }
    }
    linkRefs.add(new LinkReference(rel, styleLink, type));
  }
  
  
  public void addStyleLink (File styleLink) {
    for (LinkReference link : linkRefs) {
      if (link.getHref().equals(styleLink)) {
        return;
      }
    }
    linkRefs.add(new LinkReference("stylesheet", styleLink, "text/css"));
  }
  
  
  public void addStyleContent (String styleContent) {
    styleContents.add(styleContent);
  }
  
  
  List<String> getStyleContents () {
    return styleContents;
  }
  
  
  List<LinkReference> getLinkRefs () {
    return linkRefs;
  }
  
  
  public void setBodyContent (String s) {
    bodyContent.setLength(0);
    bodyContent.append(s);
  }
  
  
  public void append (String s) {
    bodyContent.append(s);
  }
  
  
  public String getBodyContent () {
    return bodyContent.toString();
  }
  
}
