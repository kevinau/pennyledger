package org.gyfor.report;

import java.util.function.Supplier;

import org.gyfor.report.level.PageLevel;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;

public class StreamReport {

  private final String ownerName;
  
  private static final int LEFT_MARGIN = 24 * 720;  
  private static final BaseFont normalFont = BaseFontFactory.getFont("Helvetica");

  public StreamReport (String ownerName) {
    this.ownerName = ownerName;
  }
  
  public void generate(Supplier<?> supplier, String fileName, PaperSize paperSize) {
    PDFReportPager pager = new PDFReportPager(fileName, paperSize);
    
    IReportLevel pageLevel = new PageLevel(pager, ownerName);
    
    ReportEngine reportEngine = new ReportEngine(pager);
    reportEngine.addHeader(pageLevel);
    
    Object line = supplier.get();
    while (line != null) {
      final String linex = line.toString();
      
      IReportBlock detail = new PDFReportBlock(pager) {
        @Override
        public int getHeight() {
          return normalFont.getLineHeight(10);
        }

        @Override
        public void emit(int offset) {
          PDFContent pdfContent = getContent();
          
          pdfContent.beginText();
          pdfContent.setFontAndSize(normalFont, 10);
          pdfContent.setNonStrokeGrey(0);
          int baseLineOffset = normalFont.getAboveBaseLine(10);
          System.out.println(offset + "  " + (offset + baseLineOffset) + "  " + linex);
          pdfContent.drawText(LEFT_MARGIN, offset + baseLineOffset, linex);
          pdfContent.endText();
        }
      };
      reportEngine.addDetail(detail);
      reportEngine.addDetail(detail);
      line = supplier.get();
    }
    
    reportEngine.addFooter(pageLevel);
    reportEngine.close();
  }

}
