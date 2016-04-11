package org.gyfor.report.page.pdf;

import org.gyfor.report.PaperSize;
import org.gyfor.report.page.IPagePage;


public class PDFPage extends PDFContentReference implements IPagePage {

  private final PaperSize paperSize;
  
  
  public PDFPage (PDFDocument document, PaperSize paperSize) {
    super (document, "Page");
    this.paperSize = paperSize;
    put("MediaBox", paperSize);
  }
  
  
  @Override
  public PDFContent createContent () {
    return createContent(paperSize.getMilliWidth(), paperSize.getMilliHeight());
  }

}
