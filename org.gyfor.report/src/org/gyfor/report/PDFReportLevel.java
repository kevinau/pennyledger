package org.gyfor.report;

import java.io.IOException;

import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;
import org.junit.Test;

public class PDFReportLevel {

  private final int level;
  private final BaseFont normalFont;
  private final BaseFont boldFont;
  
  private PDFContent pdfContent;

  
  /**
   * Create a PDF report level. The reporting levels are:
   * <ul>
   * <li>0.  Detail.  10pt Helvetica.</li>
   * <li>1.  Minor totals.  10pt Helvetica.  A fine gray line preceding.  Only used
   * when there are 3 or more report levels (excluding detail).</li>
   * <li>2.  Normal totals.  10pt Helvetica bold.  A fine black line preceding.  Used in all report levels.</li>
   * <li>3.  Second level totals.  12pt Helvetica bold.  A medium black line preceding.  Used when there
   * are two or more report levels (excluding detail).</li>
   * <li>4.  Maximum level totals.  14pt Helvetica bold.  A thickish black line preceding.  Only used when there
   * are 4 or more report levels (excluding detail).</li>
   * </ul>
   */
  public PDFReportLevel (int level) {
    this.level = level;
    normalFont = BaseFontFactory.getFont("Helvetica");
    boldFont = BaseFontFactory.getFont("Helvetica Bold");
  }
  
  
  @Test
  public void test() throws IOException {
    PDFReportPager pager = new PDFReportPager("test.pdf", PaperSize.A4);

    ReportEngine reportEngine = new ReportEngine(pager);
    IReportBlock detail = new PDFReportBlock(pager);
    reportEngine.addDetail(detail);
    reportEngine.close();
  }


  public void emit(int offset, int level, String label) {
    pdfContent.beginText();
    switch (level) {
    case 0 :
      pdfContent.setFontAndSize(normalFont, 10);
      break;
    case 1 :
      pdfContent.setFontAndSize(normalFont, 10);
      break;
    case 2 :
      pdfContent.setFontAndSize(boldFont, 10);
      break;      
    case 3 :
      pdfContent.setFontAndSize(boldFont, 12);
      break;      
    case 4 :
      pdfContent.setFontAndSize(boldFont, 14);
      break;      
    }
    pdfContent.setNonStrokeGrey(0);
    
    int y = 20000;
    pdfContent.drawText(20000, y, "The quick brown fox");
    pdfContent.endText();
  }

}
