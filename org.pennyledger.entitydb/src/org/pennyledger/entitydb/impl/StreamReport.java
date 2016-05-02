package org.pennyledger.entitydb.impl;

import java.util.function.Supplier;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.PDFReportBlock;
import org.gyfor.report.PDFReportPager;
import org.gyfor.report.PaperSize;
import org.gyfor.report.ReportingEngine;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;

public class StreamReport {

  private final String ownerName;
  
  private static final int LEFT_MARGIN = 30 * 720;  
  private static final BaseFont normalFont = BaseFontFactory.getFont("Helvetica");

  public StreamReport (String ownerName) {
    this.ownerName = ownerName;
  }
  
  public void generate(Supplier<?> supplier, String fileName, PaperSize paperSize) {
    PDFReportPager pager = new PDFReportPager(fileName, paperSize);
    
    IReportGrouping pageLevel = new TitleLevel(ownerName);
    
    ReportingEngine reportEngine = new ReportingEngine(pager);
    reportEngine.printHeader(pageLevel);
    
    Object line = supplier.get();
    while (line != null) {
      final String linex = line.toString();
      
      IReportBlock detail = new IReportBlock() {
        @Override
        public int getHeight() {
          return normalFont.getLineHeight(10) * 2;
        }

        @Override
        public void emit(PDFContent canvas, int offset) {
          canvas.beginText();
          canvas.setFontAndSize(normalFont, 10);
          canvas.setNonStrokeGrey(0);
          int baseLineOffset = normalFont.getAboveBaseLine(10);
          int lineHeight = normalFont.getLineHeight(10);
          canvas.drawText(LEFT_MARGIN, offset + baseLineOffset, linex);
          canvas.drawText(LEFT_MARGIN, offset + baseLineOffset + lineHeight, linex);
          canvas.endText();
        }

        @Override
        public void setData(Object data) {
        }

      };
      reportEngine.printDetail(detail);
      line = supplier.get();
    }
    
    reportEngine.printFooter(pageLevel);
    reportEngine.close();
  }

}
