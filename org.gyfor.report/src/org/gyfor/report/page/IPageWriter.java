package org.gyfor.report.page;


public interface IPageWriter {

  public void close();

  
  public void newPage();

  
  //public void writeTemplate(PageCountTemplate templ);

  
  public IPageDocument getDocument();
  
  
  public IPageContent getPageContent();
  

  public IPageTemplate createTemplate(int x0, int y0, int x1, int y1);

  
  public int getPageWidth();

  
  public int getPageHeight();


  public int getPageNumber();

}
