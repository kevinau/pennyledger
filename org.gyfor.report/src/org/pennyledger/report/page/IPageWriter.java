package org.pennyledger.report.page;


public interface IPageWriter {

  public void close();

  
  public void newPage();

  
  public IPageDocument getDocument();
  
  
  public IPageContent getPageContent();
  

  public int getPageWidth();

  
  public int getPageHeight();


  public int getPageNumber();

}
