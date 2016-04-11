package org.gyfor.report.page;

import java.awt.Color;

public interface IPageContent {

  public final static int LINE_CAP_BUTT = 0;
  public final static int LINE_CAP_ROUND = 1;
  public final static int LINE_CAP_SQUARE = 2;
  
  public final static int LINE_JOIN_MITRE = 0;
  public final static int LINE_JOIN_ROUND = 1;
  public final static int LINE_JOIN_BEVEL = 2;
  
  public void beginText();
  
  public void closeAndFill();
  
  public void closeAndStroke();
  
  public void optionalLineBreak();
  
  public void drawImage(IPageImage image, int x, int y, int w);
  
  public void drawTemplate(IPageTemplate template);
  
  public void drawTemplate(IPageTemplate template, int x, int y);

  public void setNonStrokeGrey(int grey);
  
  public void setStrokeGrey(int grey);
  
  public void textTransform (int a, int b, int c, int d, int x, int y);
  
  public void drawChar(char c);
  
  public int getPaperWidth();

  public int getPaperHeight();

  public void drawText(int x, int y, String value);
  
  public void endText();

  public void lineTo(int x, int y);
  
  public void moveTo(int x, int y);
  
  public void rectangle(int x, int y, int w, int h);

  public void restoreGraphicState();

  public void saveGraphicState();
  
  public void setFontAndSize(BaseFont font, float fontSize);
  
  public void setLineCapStyle(int style);

  public void setLineDashPattern(int style, int... pattern);
  
  public void setLineJoinStyle(int style);

  public void setLineStyle(int phase, LineStyle style, int width);

  public void setLineWidth(int width);

  public void setNonStrokeColor(int color);

  public void drawTextCentered(int x, int y, String content, int w);

  public void drawTextRight(int y, String content, int x1);

  public void setStrokeColor(int color);

  public void setNonStrokeColor(Color color);

  public void setStrokeColor(Color color);

  public void stroke();

}
