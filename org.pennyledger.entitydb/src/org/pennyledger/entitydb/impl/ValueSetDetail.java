package org.pennyledger.entitydb.impl;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;

public class ValueSetDetail implements IReportBlock {

  private final IValueSet valueSet;
  private final ColumnWidth[] widths;
  
  private static final int LEFT_MARGIN = 30 * 720;
  private static final int pageHeadingGrey = 0;
  private static final float pageHeadingFontSize = 9;
  
  private static final BaseFont pageHeadingFont = BaseFontFactory.getFont("Helvetica");
  
  public ValueSetDetail(IValueSet valueSet) {
    this.valueSet = valueSet;
    
    widths = new ColumnWidth[valueSet.size()];
    for (int i = 0; i < widths.length; i++) {
      widths[i] = new ColumnWidth();
    }
  }
  
  
  @Override
  public int getHeight() {
    return pageHeadingFont.getLineHeight(pageHeadingFontSize);
  }

  
  @Override
  public void calcWidth() {
    for (int i = 0; i < valueSet.size(); i++) {
      Object v = valueSet.get(i);
      String vx = valueSet.typeOf(i).toValueString(v);
      int width = pageHeadingFont.getAdvance(vx, pageHeadingFontSize);
      widths[i].consider(width);
    }
  }

  
  @Override
  public void emit(PDFContent canvas, int offset) {
    double margin = LEFT_MARGIN;
    
    for (int i = 0; i < valueSet.size(); i++) {
      double width = widths[i].getMax();

      Object v = valueSet.get(i);
      String vx = valueSet.typeOf(i).toValueString(v);

      canvas.beginText();
      canvas.setFontAndSize(pageHeadingFont, pageHeadingFontSize);
      canvas.setNonStrokeGrey(pageHeadingGrey);
      canvas.drawText((int)margin, offset, vx);
      canvas.endText();
        
      margin += width;
    }
  }

}
