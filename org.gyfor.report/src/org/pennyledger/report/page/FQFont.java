package org.pennyledger.report.page;

import java.awt.Color;

public class FQFont {

  private final BaseFont baseFont;
  private final float fontSize;
  private final Color colour;
  private final int grey;
  
  
  public FQFont (String fontName, int weight, int style, float fontSize, Color colour) {
    baseFont = BaseFontFactory.getFont(fontName, weight, style);
    this.fontSize = fontSize;
    this.colour = colour;
    this.grey = 0;
  }
  
  
  public FQFont (String fontName, float fontSize, Color colour) {
    this(fontName, BaseFont.MEDIUM, BaseFont.UPRIGHT, fontSize, colour);
  }
  
  
  public FQFont (String fontName, int weight, int style, float fontSize, int grey) {
    baseFont = BaseFontFactory.getFont(fontName, weight, style);
    this.fontSize = fontSize;
    this.colour = null;
    this.grey = grey;
  }
  
  
  public FQFont (String fontName, float fontSize, int grey) {
    this(fontName, BaseFont.MEDIUM, BaseFont.UPRIGHT, fontSize, grey);
  }
  
  
  public FQFont (String fontName, float fontSize) {
    this(fontName, BaseFont.MEDIUM, BaseFont.UPRIGHT, fontSize, Greys.BLACK);
  }
  
  
  public int getAdvance(String s) {
    return baseFont.getAdvance(s, fontSize);
  }
  
  
  public int getAboveBaseLine() {
    return baseFont.getAboveBaseLine(fontSize);
  }
  
  
  public int getLineHeight() {
    return baseFont.getLineHeight(fontSize);
  }
  
  
  public BaseFont getBaseFont() {
    return baseFont;
  }


  public float getFontSize() {
    return fontSize;
  }
  
  
  public Color getColor() {
    return colour;
  }
  
  
  public boolean isGrey() {
    return colour == null;
  }
  
  
  public int getGrey() {
    return grey;
  }
  
}


