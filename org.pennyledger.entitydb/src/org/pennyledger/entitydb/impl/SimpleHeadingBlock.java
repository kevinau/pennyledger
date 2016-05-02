package org.pennyledger.entitydb.impl;

import java.lang.reflect.Field;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.Greys;
import org.gyfor.report.page.pdf.PDFContent;
import org.pennyledger.object.type.IType;


public class SimpleHeadingBlock implements IReportBlock {

  private static final int LEFT_MARGIN = 30 * 720;

  private static final int headingGrey = Greys.BLACK;
  private static final int labelGrey = Greys.MID;
  private static final float headingFontSize = 9;
  
  private static final BaseFont headingFont = BaseFontFactory.getFont("Helvetica");
  private static final int labelDataGap = headingFont.getEmWidth(headingFontSize);
  
  private final String[] labels;
  private final IType<Object>[] types;
  private final Field[] fields;
  private final int maxLabelWidth;
  
  private Object data;
  
  
  public SimpleHeadingBlock (String[] labels, IType<Object>[] types, Field[] fields) {
    this.labels = labels;
    this.types = types;
    this.fields = fields;
    
    int mx = 0;
    for (String label : labels) {
      int w = headingFont.getAdvance(label, headingFontSize);
      mx = Math.max(mx, w);
    }
    maxLabelWidth = mx;
  }

  
  @Override
  public int getHeight() {
    // Heading lines, then a half a blank line
    double n = labels.length + 0.5;
    return (int)(headingFont.getLineHeight(headingFontSize) * n);
  }


  @Override
  public void calcWidths() {
  }


  @Override
  public void emit(PDFContent canvas, int offset) {
    int baseLineOffset = headingFont.getAboveBaseLine(headingFontSize);
    offset += baseLineOffset;
    int lineHeight = headingFont.getLineHeight(headingFontSize);

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

      canvas.beginText();
      canvas.setFontAndSize(headingFont, headingFontSize);
      canvas.setNonStrokeGrey(labelGrey);
      canvas.drawTextRight(offset, labels[i], LEFT_MARGIN + maxLabelWidth);
      canvas.endText();
     
      canvas.beginText();
      canvas.setFontAndSize(headingFont, headingFontSize);
      canvas.setNonStrokeGrey(headingGrey);
      canvas.drawText(LEFT_MARGIN + maxLabelWidth + labelDataGap, offset, valuex);
      canvas.endText();
     
      offset += lineHeight;
      i++;
    }
  }

 
  @Override
  public void setData (Object data) {
    this.data = data;
  }

}
