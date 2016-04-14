package org.gyfor.report;

import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;

public class PDFReportBlock implements IReportBlock {

  private final PDFReportPager pager;
  

  public PDFReportBlock (PDFReportPager pager) {
    this.pager = pager;
  }

  
  protected PDFContent getContent () {
    return pager.getContent();
  }
  

  @Override
  public int getHeight() {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public void emit(int offset) {
    PDFContent pdfContent = getContent();
    
    pdfContent.beginText();
    int y = 20000;
    BaseFont normalFont = BaseFontFactory.getFont("Helvetica");
    pdfContent.setFontAndSize(normalFont, 10);
    pdfContent.setNonStrokeGrey(0);
    pdfContent.drawText(20000, y, "The quick brown fox");
    pdfContent.endText();
  }


  @Override
  public boolean isMandatory() {
    return false;
  }


  @Override
  public void setData(Object data) {
    // TODO Auto-generated method stub
    
  }

}
