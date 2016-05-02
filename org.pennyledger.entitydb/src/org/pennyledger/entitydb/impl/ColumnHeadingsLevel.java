package org.pennyledger.entitydb.impl;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.Greys;
import org.gyfor.report.page.pdf.PDFContent;
import org.pennyledger.object.type.IType;


public class ColumnHeadingsLevel<T> implements IReportGrouping<T> {

  private static final int LEFT_MARGIN = 30 * 720;

  private static final int headingsFontGrey = Greys.MID;
  private static final float headingsFontSize = 6;
  private static final BaseFont headingsFont = BaseFontFactory.getFont("Helvetica");

  private static final int underlineWidth = 100;
  
  private final IReportBlock headingBlock;

  private String[] labels = new String[0];
  @SuppressWarnings("unchecked")
  private IType<Object>[] types = new IType[0];
  private ColumnSet colSet;
  

  public ColumnHeadingsLevel() {
    headingBlock = new IReportBlock() {
      @Override
      public int getHeight() {
        if (labels.length == 0) {
          return 0;
        } else {
          return (int)(headingsFont.getLineHeight(headingsFontSize) * 1.5);
        }
      }


      @Override
      public void calcWidths() {
        // Do nothing here. Width calculation is done when finalizeWidths is
        // called.
      }


      @Override
      public void emit(PDFContent canvas, int voffset) {
        int baseLineOffset = headingsFont.getAboveBaseLine(headingsFontSize);
        int voffset1 = voffset + baseLineOffset;

        canvas.beginText();
        canvas.setFontAndSize(headingsFont, headingsFontSize);
        canvas.setNonStrokeGrey(headingsFontGrey);

        int i = 0;
        int hpos = LEFT_MARGIN;
        for (String label : labels) {
          IType<Object> type = types[i];

          int width = (int)colSet.getWidth(i);
          //int leadin = (int)colSet.getHeadingLead(i);
          //boolean undersize = colSet.isHeadingSmall(i);
          
          switch (type.getAlignment()) {
          case NUMERIC :
            double width1 = colSet.getMax1(i);
            int m = (int)(label.length() * width1 / width);
            canvas.drawTextAligned(hpos /* + leadin */, voffset1, label, m, (int)width1);
            break;
          case LEFT :
            canvas.drawText(hpos, voffset1, label);
            break;
          case CENTRE :
            canvas.drawTextCentered(hpos, voffset1, label, width);
            break;
          case RIGHT :
            canvas.drawTextRight(voffset1, label, hpos + width);
            break;
          }

          hpos += width + colSet.getGap();
          i++;
        }
        canvas.endText();
        
        int lineHeight = headingsFont.getLineHeight(headingsFontSize);
        int voffset2 = voffset + lineHeight;
        canvas.setLineWidth(underlineWidth);
        canvas.setStrokeGrey(headingsFontGrey);
        canvas.moveTo(LEFT_MARGIN, voffset2);
        canvas.lineTo(hpos - colSet.getGap(), voffset2);
        canvas.stroke();
      }
    };
  }


  public void setHeadingLabels(String[] labels, IType<Object>[] types, ColumnSet colSet) {
    this.labels = labels;
    this.types = types;
    this.colSet = colSet;
  }


  @Override
  public Object getGroup(T source) {
    return "";
  }


  @Override
  public IReportBlock getLogicalHeader() {
    return headingBlock;
  }


  @Override
  public IReportBlock getPhysicalHeader() {
    return headingBlock;
  }


  @Override
  public IReportBlock getLogicalFooter() {
    return null;
  }


  @Override
  public IReportBlock getPhysicalFooter() {
    return null;
  }


  @Override
  public void setData(Object source) {
    headingBlock.setData(source);
  }


  @Override
  public void finalizeWidths() {
    int i = 0;
    for (String label : labels) {
      int labelWidth = headingsFont.getAdvance(label, headingsFontSize);
      colSet.noteHeadingWidth(i, labelWidth);
      i++;
    }
  }

}
