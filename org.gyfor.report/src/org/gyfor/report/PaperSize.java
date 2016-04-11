/*******************************************************************************
 * Copyright (c) 2008 Kevin Holloway (kholloway@geckosoftware.com.au).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.txt
 * 
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 ******************************************************************************/
package org.gyfor.report;

public enum PaperSize {
  A0 (841, 1189),
  A1 (594, A0.width),
  A2 (420, A1.width),
  A3 (297, A2.width),
  A4 (210, A3.width),
  A4R (A4.height, A4.width),
  A5 (148, A4.width),
  A6 (105, A5.width),
  A7 (74, A6.width);
  
  private int width;
  private int height;
  
  private PaperSize (int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  public float getWidth () {
    return (float)(width / 25.4) * 72;
  }
  
  public float getHeight () {
    return (float)(height / 25.4) * 72;
  }
  
  
  public int getMilliWidth () {
    return (int)(getWidth() * 1000 + 0.5);
  }
  
  
  public int getMilliHeight () {
    return (int)(getHeight() * 1000 + 0.5);
  }
  
  
  @Override
  public String toString() {
    return "PaperSize[" + width + "," + height + "]";
  }
}
