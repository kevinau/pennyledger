package org.pennyledger.report.page;

import java.awt.Color;

public class FQLine {

  public static final FQLine THIN = new FQLine(300, Greys.BLACK);
  public static final FQLine MEDIUM = new FQLine(600, Greys.BLACK);
  public static final FQLine THICK = new FQLine(1200, Greys.BLACK);
  
  private final int width;
  private final Color colour;
  private final int grey;
  
  
  public FQLine (int width, Color colour) {
    this.width = width;
    this.colour = colour;
    this.grey = 0;
  }
  
  
  public FQLine (int width) {
    this (width, Greys.WHITE);
  }
  
  
  public FQLine (int width, int grey) {
    this.width = width;
    this.colour = null;
    this.grey = grey;
  }
  
  
  public int getWidth() {
    return width;
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
