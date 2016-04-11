package org.gyfor.report.page;

import java.io.InputStream;

import org.gyfor.report.PaperSize;


public interface IPageDocument {

  public void close();
  
  
  public IPageImage createImage(String imageFileName, InputStream imageInputStream);
  
  
  public IPagePage createPage(PaperSize paperSize);
  
  
  public IPageTemplate createTemplate(int i, int j, int milliWidth, int milliHeight);
  
  
  public void setAuthor(String name);

  
  public void setTitle(String title);
  
}
