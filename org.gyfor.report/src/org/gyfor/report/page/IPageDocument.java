package org.gyfor.report.page;

import java.io.InputStream;

import org.gyfor.report.PaperSize;
import org.gyfor.report.page.pdf.PDFIndirect;


public interface IPageDocument {

  public void close();


  public IPageImage createImage(String imageFileName, InputStream imageInputStream);


  public IPagePage createPage(PaperSize paperSize);


  public IPageTemplate createTemplate();


  public void setAuthor(String name);


  public void setTitle(String title);


  public PDFIndirect createIndirect(PaperSize paperSize);

}
