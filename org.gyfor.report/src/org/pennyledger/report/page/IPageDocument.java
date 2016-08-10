package org.pennyledger.report.page;

import java.io.InputStream;

import org.pennyledger.report.PaperSize;
import org.pennyledger.report.page.pdf.PDFIndirect;


public interface IPageDocument {

  public void close();


  public IPageImage createImage(String imageFileName, InputStream imageInputStream);


  public IPagePage createPage(PaperSize paperSize);


  public IPageTemplate createTemplate();


  public void setAuthor(String name);


  public void setTitle(String title);


  public PDFIndirect createIndirect(PaperSize paperSize);

}
