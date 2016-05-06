package org.pennyledger.entitydb.impl;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.Greys;
import org.gyfor.report.page.pdf.PDFContent;
import org.junit.Assert;
import org.pennyledger.object.type.Alignment;
import org.pennyledger.object.type.IType;


public class ColumnHeadingsLevel<T> implements IReportGrouping<T> {

  private static class StringPair {
    final String s1;
    final String s0;
    final int w1;
    final int w0;


    private StringPair(String s1, int w1, String s0, int w0) {
      this.s1 = s1;
      this.w1 = w1;
      this.s0 = s0;
      this.w0 = w0;
    }


    private StringPair(String s0, int w0) {
      this.s1 = null;
      this.w1 = 0;
      this.s0 = s0;
      this.w0 = w0;
    }
    
    private int maxWidth() {
      if (s1 == null) {
        return w0;
      } 
      return Math.max(w0,  w1);
    }
  }

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

  private StringPair[] labelPairs;
  private int lineCount = 1;


  public ColumnHeadingsLevel() {
    headingBlock = new IReportBlock() {
      @Override
      public int getHeight() {
        if (labels.length == 0) {
          return 0;
        } else {
          int lineHeight = (int)(headingsFont.getLineHeight(headingsFontSize));
          return lineHeight * (lineCount + 1) + underlineWidth + lineHeight / 2;
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
        int lineHeight = headingsFont.getLineHeight(headingsFontSize);
        int voffset1 = voffset + lineHeight + baseLineOffset;
        int hpos = 0;

        for (int j = lineCount - 1; j >= 0; j--) {
          canvas.beginText();
          canvas.setFontAndSize(headingsFont, headingsFontSize);
          canvas.setNonStrokeGrey(headingsFontGrey);
          
          int i = 0;
          hpos = LEFT_MARGIN;
          for (StringPair labelPair : labelPairs) {
            ColumnWidth colWidth = colSet.get(i);
            int width = (int)colWidth.getWidth();

            String label = (j == 1 ? labelPair.s1 : labelPair.s0);
            if (label != null) {
              IType<Object> type = types[i];
              int aveData = (int)colWidth.getAveData();
              int headingWidth = (int)colWidth.getMaxHeading();
              int maxData = (int)colWidth.getMaxData();
              int maxData1 = (int)colWidth.getMax1();
              int headingCentre;
              
              if (headingWidth > maxData) {
                // Heading size is greater than data width
                headingCentre = headingWidth / 2;
              } else {
                // Heading size is less than data width
                int slack = 0;
                switch (type.getAlignment()) {
                case LEFT :
                  slack = 0;
                  break;
                case CENTRE :
                  slack = (width - aveData) / 2;
                  break;
                case RIGHT :
                  slack = width - aveData;
                  break;
                case NUMERIC :
                  slack = (width - aveData) * maxData1 / width;
                  break;
                }
                headingCentre = width / 2 + slack;
              }
              canvas.drawTextCentered(hpos + headingCentre, voffset1, label);
            }
            
            hpos += width + colSet.getGap();
            i++;
          }
          canvas.endText();
          voffset1 += lineHeight;
        }

        int voffset2 = voffset + lineHeight * (lineCount + 1);
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
    labelPairs = new StringPair[labels.length];

    int i = 0;
    for (String label : labels) {
      StringPair labelPair;

      int labelWidth = headingsFont.getAdvance(label, headingsFontSize);
      int dataWidth = (int)colSet.getWidth(i);
      if (labelWidth > dataWidth) {
        labelPair = splitLabel(label, labelWidth);
      } else {
        labelPair = new StringPair(label, labelWidth);
      }
      labelPairs[i] = labelPair;
      colSet.noteHeadingWidth(i, labelPair.w1);
      colSet.noteHeadingWidth(i, labelPair.w0);
      if (labelPair.s1 != null) {
        lineCount = 2;
      }
      i++;
    }
  }


  private static StringPair splitLabel(String label, int labelWidth) {
    int minVariance = Integer.MAX_VALUE;
    StringPair minLabels = new StringPair(label, labelWidth);

    int n0 = 0;
    int n = label.indexOf(' ', n0);
    while (n != -1) {
      String s1 = label.substring(0, n);
      String s0 = label.substring(n + 1);
      int w1 = headingsFont.getAdvance(s1, headingsFontSize);
      int w0 = headingsFont.getAdvance(s0, headingsFontSize);
      int variance = Math.abs(w1 - w0);
      if (variance < minVariance) {
        minLabels = new StringPair(s1, w1, s0, w0);
        minVariance = variance;
      }
      n0 = n + 1;
      n = label.indexOf(' ', n0);
    }
    return minLabels;
  }

}
