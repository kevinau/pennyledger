package org.gyfor.report.page;

public interface IPageTemplate {

  public int getHeight();
  
  public IPageContent createContent();
  
  public void close();
  
  @Override
  public String toString();
  
}
