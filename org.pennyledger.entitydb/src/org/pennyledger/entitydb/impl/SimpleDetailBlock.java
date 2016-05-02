package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.Greys;
import org.gyfor.report.page.pdf.PDFContent;
import org.pennyledger.object.type.IType;


public class SimpleDetailBlock implements IReportBlock {

  private static final int LEFT_MARGIN = 30 * 720;

  private static final int detailGrey = Greys.BLACK;
  private static final float detailFontSize = 9;
  private static final BaseFont detailFont = BaseFontFactory.getFont("Helvetica");
  
  private final String[] labels;
  private final IType<Object>[] types;
  private final Field[] fields;
  private final ColumnSet colSet;
  
  private Object data;
  
  
  public SimpleDetailBlock (String[] labels, IType<Object>[] types, Field[] fields, ColumnSet colSet) {
    this.labels = labels;
    this.types = types;
    this.fields = fields;
    this.colSet = colSet;
  }

  
  @Override
  public int getHeight() {
    // Not yet allowing for line breaks because of column overflow TODO
    return detailFont.getLineHeight(detailFontSize);
  }


  @Override
  public void calcWidths() {
    int i = 0;
    for (Field field : fields) {
      Object value;
      try {
        value = field.get(data);
      } catch (IllegalArgumentException | IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
      IType<Object> type = types[i];
      String valuex = type.toValueString(value);
      
      int width1;
      int width2 = 0;
      switch (type.getAlignment()) {
      case NUMERIC :
        int n = valuex.lastIndexOf('.');
        if (n >= 0) {
          width1 = detailFont.getAdvance(valuex.substring(0,  n), detailFontSize);
          width2 = detailFont.getAdvance(valuex.substring(n), detailFontSize);
        } else {
          int m = valuex.length();
          while (m > 0 && !Character.isDigit(valuex.charAt(m - 1))) {
            m--;
          }
          if (m > 0 && m < valuex.length()) {
            width1 = detailFont.getAdvance(valuex.substring(0,  m), detailFontSize);
            width2 = detailFont.getAdvance(valuex.substring(m), detailFontSize);
          } else {
            // This is strange.  We have a field marked as numeric, but it contains
            // no digits.  Treat it as a simple, right aligned value
            width1 = detailFont.getAdvance(valuex, detailFontSize);
          }
        }
        break;
      default :
        width1 = detailFont.getAdvance(valuex, detailFontSize);
        break;
      }
      colSet.noteDataWidth(i, width1, width2);
      i++;
    }
  }

  
  @Override
  public void emit(PDFContent canvas, int offset) {
    int baseLineOffset = detailFont.getAboveBaseLine(detailFontSize);
    offset += baseLineOffset;

    canvas.beginText();
    canvas.setFontAndSize(detailFont, detailFontSize);
    canvas.setNonStrokeGrey(detailGrey);

    int i = 0;
    int leftMargin = LEFT_MARGIN;
    for (Field field : fields) {
      Object value;
      try {
        value = field.get(data);
      } catch (IllegalArgumentException | IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
      IType<Object> type = types[i];
      String valuex = type.toValueString(value);
      int width = (int)colSet.getWidth(i);
      int width1 = (int)colSet.getMax1(i);
      int leadin = (int)colSet.getDataLead(i);
      
      switch (type.getAlignment()) {
      case NUMERIC :
        int n = valuex.lastIndexOf('.');
        if (n >= 0) {
          canvas.drawTextAligned(leftMargin + leadin, offset, valuex, n, width1);
        } else {
          int m = valuex.length();
          while (m > 0 && !Character.isDigit(valuex.charAt(m - 1))) {
            m--;
          }
          if (m > 0 && m < valuex.length()) {
            canvas.drawTextAligned(leftMargin + leadin, offset, valuex, m, width1);
          } else {
            canvas.drawTextRight(offset, valuex, leftMargin + width - leadin);
          }
        }
        break;
      case LEFT :
        canvas.drawText(leftMargin + leadin, offset, valuex);
        break;
      case CENTRE :
        canvas.drawTextCentered(leftMargin, offset, valuex, width - leadin * 2);
        break;
      case RIGHT :
        canvas.drawTextRight(offset, valuex, leftMargin + width - leadin);
        break;
      }

      leftMargin += width + colSet.getGap();
      i++;
    }
    canvas.endText();
  }

 
  @Override
  public void setData (Object data) {
    this.data = data;
  }

}
