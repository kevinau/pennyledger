package org.pennyledger.reportdb;

import org.pennyledger.report.IReportBlock;
import org.pennyledger.report.PaperSize;
import org.pennyledger.report.page.pdf.PDFContent;

public class BlankReportBlock implements IReportBlock {

  private final int height;
  
  public BlankReportBlock (int height) {
    this.height = height;
  }
  
  
  @Override
  public int getHeight() {
    return height;
  }

  
  @Override
  public void emit(PDFContent canvas, int offset) {
    canvas.saveGraphicState();
    canvas.setNonStrokeGrey(128 + 64 + 32);
    canvas.rectangle(0, offset, PaperSize.A4.getMilliWidth(), height);
    canvas.fill();
    canvas.restoreGraphicState();
  }

  
  @Override
  public void calcWidths() {
  }

}
