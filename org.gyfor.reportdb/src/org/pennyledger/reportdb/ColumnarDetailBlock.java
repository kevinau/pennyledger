package org.pennyledger.reportdb;

import org.pennyledger.object.type.IType;
import org.pennyledger.report.IReportBlock;
import org.pennyledger.report.page.FQFont;
import org.pennyledger.report.page.FQLine;
import org.pennyledger.report.page.pdf.PDFContent;

public abstract class ColumnarDetailBlock implements IReportBlock {

  private final IType<Object>[] types;
  private final String[] names;
  private final ColumnSet colSet;
  
  
  public ColumnarDetailBlock (IType<Object>[] types, String[] names, ColumnSet colSet) {
    this.types = types;
    this.names = names;
    this.colSet = colSet;
  }

  
  public int getHeight(FQFont font, FQLine line) {
    int height = 0;
    if (line != null) {
      height += line.getWidth() * 2;
    }
    // Not yet allowing for line breaks because of column overflow TODO
    height += font.getLineHeight();
    return height;
  }


  protected void calcWidths(FQFont font) {
    for (int i = 0; i < names.length; i++) {
      Object value = getValue(i);
      IType<Object> type = types[i];
      String valuex = type.toValueString(value);
      
      int width1;
      int width2 = 0;
      switch (type.getAlignment()) {
      case NUMERIC :
        int n = valuex.lastIndexOf('.');
        if (n >= 0) {
          width1 = font.getAdvance(valuex.substring(0,  n));
          width2 = font.getAdvance(valuex.substring(n));
        } else {
          int m = valuex.length();
          while (m > 0 && !Character.isDigit(valuex.charAt(m - 1))) {
            m--;
          }
          if (m > 0 && m < valuex.length()) {
            width1 = font.getAdvance(valuex.substring(0,  m));
            width2 = font.getAdvance(valuex.substring(m));
          } else {
            // This is strange.  We have a field marked as numeric, but it contains
            // no digits.  Treat it as a simple, right aligned value
            width1 = font.getAdvance(valuex);
          }
        }
        break;
      default :
        width1 = font.getAdvance(valuex);
        break;
      }
      colSet.noteDataWidth(names[i], width1, width2);
    }
  }


  private void drawLine (PDFContent canvas, int offset, FQLine line, int beginPosn, int endPosn) {
    canvas.setLineWidth(line.getWidth());
    if (line.isGrey()) {
      canvas.setStrokeGrey(line.getGrey());
    } else {
      canvas.setStrokeColor(line.getColor());
    }
    canvas.moveTo(beginPosn, offset);
    canvas.lineTo(endPosn, offset);
    canvas.stroke();
  }
  
  
  public void emit(PDFContent canvas, int offset, FQFont font, FQLine line) {
    if (line != null) {
      int lastStart = Integer.MIN_VALUE;
      int lastEnd = Integer.MIN_VALUE;
      
      for (int i = 0; i < names.length; i++) {
        ColumnWidth colWidth = colSet.get(names[i]);
        int width = (int)colWidth.getWidth();
        int startPosn = colWidth.getStartPosition();
        if (lastStart == Integer.MIN_VALUE) {
          lastStart = startPosn;
        } else if (lastEnd + colSet.getGap() < startPosn) {
          drawLine(canvas, offset, line, lastStart, lastEnd);
        }
        lastEnd = startPosn + width;
      }
      if (lastStart != Integer.MIN_VALUE) {
        drawLine(canvas, offset, line, lastStart, lastEnd);
      }
      offset += line.getWidth() * 2;
    }
    
    int baseLineOffset = font.getAboveBaseLine();
    offset += baseLineOffset;

    canvas.beginText();
    canvas.setFQFont(font);

    for (int i = 0; i < names.length; i++) {
      Object value = getValue(i);
      IType<Object> type = types[i];
      String valuex = type.toValueString(value);
      ColumnWidth colWidth = colSet.get(names[i]);
      int width = (int)colWidth.getWidth();
      int width1 = (int)colWidth.getMax1();
      int leadin = (int)colWidth.getDataLead();
      int beginPosn = colWidth.getStartPosition();
      
      switch (type.getAlignment()) {
      case NUMERIC :
        int n = valuex.lastIndexOf('.');
        if (n >= 0) {
          canvas.drawTextAligned(beginPosn + leadin, offset, valuex, n, width1);
        } else {
          int m = valuex.length();
          while (m > 0 && !Character.isDigit(valuex.charAt(m - 1))) {
            m--;
          }
          if (m > 0 && m < valuex.length()) {
            canvas.drawTextAligned(beginPosn + leadin, offset, valuex, m, width1);
          } else {
            canvas.drawTextRight(offset, valuex, beginPosn + width - leadin);
          }
        }
        break;
      case LEFT :
        canvas.drawText(beginPosn + leadin, offset, valuex);
        break;
      case CENTRE :
        canvas.drawTextCentered(beginPosn, offset, valuex, width - leadin * 2);
        break;
      case RIGHT :
        canvas.drawTextRight(offset, valuex, beginPosn + width - leadin);
        break;
      }
    }
    canvas.endText();
  }


  protected abstract Object getValue(int i);

}
