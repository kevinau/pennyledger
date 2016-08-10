package org.pennyledger.reportdb;

import java.lang.reflect.Field;

import org.pennyledger.object.type.IType;
import org.pennyledger.report.IReportBlock;
import org.pennyledger.report.page.BaseFont;
import org.pennyledger.report.page.BaseFontFactory;
import org.pennyledger.report.page.Greys;
import org.pennyledger.report.page.pdf.PDFContent;


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
    int lineHeight = (int)(headingFont.getLineHeight(headingFontSize));
    return lineHeight / 2 + lineHeight * labels.length;
  }


  @Override
  public void calcWidths() {
  }


  @Override
  public void emit(PDFContent canvas, int offset) {
    int baseLineOffset = headingFont.getAboveBaseLine(headingFontSize);
    int lineHeight = headingFont.getLineHeight(headingFontSize);
    offset += lineHeight / 2 + baseLineOffset;

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
