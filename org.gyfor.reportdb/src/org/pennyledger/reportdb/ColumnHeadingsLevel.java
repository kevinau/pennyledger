package org.pennyledger.reportdb;

import org.pennyledger.object.type.IType;
import org.pennyledger.report.IReportBlock;
import org.pennyledger.report.IReportGrouping;
import org.pennyledger.report.page.FQFont;
import org.pennyledger.report.page.Greys;
import org.pennyledger.report.page.pdf.PDFContent;


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
    
//    private int maxWidth() {
//      if (s1 == null) {
//        return w0;
//      } 
//      return Math.max(w0,  w1);
//    }
  }

  private static final int LEFT_MARGIN = 30 * 720;

  private static final FQFont headingsFont = new FQFont("Helvetica", 6, Greys.MID);

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
          int lineHeight = (int)(headingsFont.getLineHeight());
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
        int baseLineOffset = headingsFont.getAboveBaseLine();
        int lineHeight = headingsFont.getLineHeight();
        int voffset1 = voffset + lineHeight + baseLineOffset;
        //int hpos = 0;
        int startPosn = 0;
        int endPosn = 0;

        for (int j = lineCount - 1; j >= 0; j--) {
          canvas.beginText();
          canvas.setFQFont(headingsFont);
          
          int i = 0;
          for (ColumnWidth colWidth : colSet) {
            StringPair labelPair = labelPairs[i];
            int width = (int)colWidth.getWidth();
            
            // Save left and right margins
            if (i == 0) {
              startPosn = colWidth.getStartPosition();
            }
            endPosn = colWidth.getStartPosition() + width;

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
              canvas.drawTextCentered(colWidth.getStartPosition() + headingCentre, voffset1, label);
            }
            i++;
          }
          canvas.endText();
          voffset1 += lineHeight;
        }

        int voffset2 = voffset + lineHeight * (lineCount + 1);
        canvas.setLineWidth(underlineWidth);
        canvas.setStrokeGrey(Greys.MID);
        canvas.moveTo(startPosn, voffset2);
        canvas.lineTo(endPosn, voffset2);
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
  public IReportBlock getPhysicalFooter() {
    return null;
  }


  @Override
  public IReportBlock getLogicalFooter() {
    return null;
  }


  @Override
  public void finalizeWidths() {
    labelPairs = new StringPair[labels.length];

    int hpos = LEFT_MARGIN;
    int i = 0;
    for (ColumnWidth cw : colSet) {
      cw.setStartPosition(hpos);

      String label = labels[i];
      StringPair labelPair;

      int labelWidth = headingsFont.getAdvance(label);
      int dataWidth = (int)cw.getWidth();
      if (labelWidth > dataWidth) {
        labelPair = splitLabel(label, labelWidth);
      } else {
        labelPair = new StringPair(label, labelWidth);
      }
      labelPairs[i] = labelPair;
      cw.noteHeadingWidth(labelPair.w1);
      cw.noteHeadingWidth(labelPair.w0);
      if (labelPair.s1 != null) {
        lineCount = 2;
      }
      
      int width = (int)cw.getWidth();
      hpos += width + colSet.getGap();
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
      int w1 = headingsFont.getAdvance(s1);
      int w0 = headingsFont.getAdvance(s0);
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


  @Override
  public void setData(T data) {
  }

}
