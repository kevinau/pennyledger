package org.gyfor.report.page;

import org.gyfor.report.page.pdf.PDFContent;

public interface IPageTemplate {

  public void close();
  
  @Override
  public String toString();

  public PDFContent createContent(int x0, int y0, int x1, int y1);
  
}
